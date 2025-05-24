package com.example.usercrud.service;

import com.example.usercrud.dtos.EnderecoDTO;
import com.example.usercrud.dtos.UsuarioCadastroDTO;
import com.example.usercrud.model.Endereco;
import com.example.usercrud.model.Usuario;
import com.example.usercrud.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public ResponseEntity<?> cadastrar(UsuarioCadastroDTO dto) {
        try {
            if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
                return ResponseEntity.badRequest().body("E-mail já cadastrado.");
            }

            if (usuarioRepository.findByCpf(dto.getCpf()).isPresent()) {
                return ResponseEntity.badRequest().body("CPF já cadastrado.");
            }

            if (!validarNome(dto.getNomeCompleto())) {
                return ResponseEntity.badRequest().body("Nome inválido. Deve conter pelo menos 2 palavras com mínimo 3 letras cada.");
            }

            Endereco enderecoFaturamento = buscarEnderecoPorCep(dto.getEnderecoFaturamento().getCep());
            preencherEndereco(enderecoFaturamento, dto.getEnderecoFaturamento());

            List<Endereco> enderecosEntrega = dto.getEnderecosEntrega().stream().map(e -> {
                try {
                    Endereco endereco = buscarEnderecoPorCep(e.getCep());
                    preencherEndereco(endereco, e);
                    return endereco;
                } catch (Exception ex) {
                    throw new RuntimeException("Erro no CEP de entrega: " + ex.getMessage());
                }
            }).collect(Collectors.toList());

            String senhaCriptografada = new BCryptPasswordEncoder().encode(dto.getSenha());

            Usuario novo = new Usuario();
            novo.setCpf(dto.getCpf());
            novo.setNomeCompleto(dto.getNomeCompleto());
            novo.setDataNascimento(dto.getDataNascimento());
            novo.setGenero(dto.getGenero());
            novo.setSenha(senhaCriptografada);
            novo.setEmail(dto.getEmail());
            novo.setEnderecoFaturamento(enderecoFaturamento);
            novo.setEnderecosEntrega(enderecosEntrega);

            Usuario salvo = usuarioRepository.save(novo);
            return ResponseEntity.ok(salvo);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erro ao cadastrar usuário: " + e.getMessage());
        }
    }


    private boolean validarNome(String nome) {
        String[] partes = nome.trim().split("\\s+");
        return partes.length >= 2 &&
                java.util.Arrays.stream(partes).allMatch(p -> p.length() >= 3);
    }

    private Endereco buscarEnderecoPorCep(String cep) throws Exception {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        RestTemplate restTemplate = new RestTemplate();
        Endereco endereco = restTemplate.getForObject(url, Endereco.class);

        if (endereco == null || endereco.getCep() == null) {
            throw new Exception("CEP inválido: " + cep);
        }
        return endereco;
    }

    private void preencherEndereco(Endereco endereco, Endereco dto) {
        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setPadrao(dto.isPadrao());
    }


    public Usuario editar(Long id, UsuarioCadastroDTO dto) throws Exception {
        Optional<Usuario> existente = usuarioRepository.findById(id);
        if (existente.isEmpty()) throw new Exception("Usuário não encontrado.");

        Usuario usuario = existente.get();

        usuario.setNomeCompleto(dto.getNomeCompleto());
        usuario.setGenero(dto.getGenero());
        usuario.setDataNascimento(dto.getDataNascimento());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(new BCryptPasswordEncoder().encode(dto.getSenha()));
        }

        if (dto.getEnderecosEntrega() != null) {
            List<Endereco> novosEnderecos = dto.getEnderecosEntrega().stream().map(e -> {
                try {
                    Endereco endereco = buscarEnderecoPorCep(e.getCep());
                    preencherEndereco(endereco, e);
                    return endereco;
                } catch (Exception ex) {
                    throw new RuntimeException("Erro no endereço: " + ex.getMessage());
                }
            }).collect(Collectors.toList());

            usuario.setEnderecosEntrega(novosEnderecos);
        }

        return usuarioRepository.save(usuario);
    }
}
