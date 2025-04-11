package com.example.usercrud.service;


import br.com.caelum.stella.validation.CPFValidator;
import br.com.caelum.stella.validation.InvalidStateException;
import com.example.usercrud.dtos.UsuarioCadastroDTO;
import com.example.usercrud.model.TiposUsuarios;
import com.example.usercrud.model.Usuario;
import com.example.usercrud.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Optional;

@Service
public class UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario cadastrar(UsuarioCadastroDTO requestDTO) {
        try {
            //aqui eu verifico se o email existe ou não no banco
            if (repository.findByEmail(requestDTO.getEmail()).isPresent()) {
                throw new RuntimeException("Email já cadastrado");
            }
            //msm coisa só que pra CPF
            if (repository.findByCpf(requestDTO.getCpf()).isPresent()) {
                throw new RuntimeException("CPF já cadastrado");
            }

            //crio o usuário
            Usuario novoUsuario = new Usuario();
            novoUsuario.setName(requestDTO.getName());
            novoUsuario.setEmail(requestDTO.getEmail());
            novoUsuario.setSenha(passwordEncoder.encode(requestDTO.getSenha()));
            novoUsuario.setCpf(requestDTO.getCpf());

          //pegar o tipo
            try {
                TiposUsuarios tipo = TiposUsuarios.valueOf(requestDTO.getTipo().toUpperCase());
                novoUsuario.setTipo(tipo);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Role inválido  ou não informado");
            }

            //definir como ativo
            novoUsuario.setAtivo(true);
            //salva usuário
            return repository.save(novoUsuario);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao cadastrar usuário: " + e.getMessage());
        }

    }

    public Usuario editar(Long id, UsuarioCadastroDTO requestDTO) {
        Optional<Usuario> usuarioOptional = repository.findById(id);
        try {
            if (usuarioOptional.isPresent()) {

                var usuarioExistente = usuarioOptional.get();

                //verifica se o email já está cadastrado por outro usuário
                if (repository.findByEmail(requestDTO.getEmail()).filter(user -> user.getId() != id).isPresent()) {
                    throw new RuntimeException("Email já cadastrado");
                }

                //verifico se o CPF já está cadastrado por outro usuário
                if (repository.findByCpf(requestDTO.getCpf()).filter(user -> user.getId() != id).isPresent()) {
                    throw new RuntimeException("CPF já cadastrado");
                }

                if (!validarCPF(requestDTO.getCpf())) {
                    throw new RuntimeException("CPF inválido");
                }

                usuarioExistente.setName(requestDTO.getName());
                usuarioExistente.setEmail(requestDTO.getEmail());

                //mudo a senha apenas se for fornecida
                if (requestDTO.getSenha() != null && !requestDTO.getSenha().isEmpty()) {
                    if (passwordEncoder.matches(requestDTO.getSenha(), usuarioExistente.getSenha())) {
                        throw new RuntimeException("A senha não pode sido usada antes");
                    }

                    usuarioExistente.setSenha(passwordEncoder.encode(requestDTO.getSenha()));
                }

                //verifico se o tipo esta presente
                if (ObjectUtils.containsConstant(TiposUsuarios.values(), requestDTO.getTipo())) {
                    TiposUsuarios tipo = TiposUsuarios.valueOf(requestDTO.getTipo().toUpperCase());
                    usuarioExistente.setTipo(tipo);
                } else {
                    throw new RuntimeException("Role inválido  ou não informado");
                }

                usuarioExistente.setAtivo(requestDTO.getAtivo());

                return repository.save(usuarioExistente);

            } else {
                throw new RuntimeException("Usuário não encontrado");
            }
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao editar usuário: " + e.getMessage());
        }

    }

    public boolean validarCPF(String CPF) {
        CPFValidator validator = new CPFValidator();
        try {
            validator.assertValid(CPF);
            return true;
        } catch (InvalidStateException e) {
            return false;
        }
    }
}

