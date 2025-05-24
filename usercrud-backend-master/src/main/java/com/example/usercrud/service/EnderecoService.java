package com.example.usercrud.service;

import com.example.usercrud.dtos.EnderecoDTO;
import com.example.usercrud.model.Endereco;
import com.example.usercrud.repository.EnderecoRepository;
import com.example.usercrud.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EnderecoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    public Endereco buscarEnderecoPorCep(String cep) throws Exception {
        String url = "https://viacep.com.br/ws/" + cep + "/json/";
        RestTemplate restTemplate = new RestTemplate();
        Endereco endereco = restTemplate.getForObject(url, Endereco.class);

        if (endereco == null || endereco.getCep() == null || endereco.getCep().isBlank()) {
            throw new Exception("CEP inválido: " + cep);
        }

        return endereco;
    }

    public Endereco montarEnderecoCompleto(EnderecoDTO dto) throws Exception {
        Endereco endereco = buscarEnderecoPorCep(dto.getCep());

        endereco.setNumero(dto.getNumero());
        endereco.setComplemento(dto.getComplemento());
        endereco.setPadrao(dto.isPadrao());

        return endereco;
    }

    // 🚀 Método para adicionar endereço
    public ResponseEntity<?> adicionarEndereco(Long usuarioId, String enderecoJson) {
        // Aqui você pode converter o JSON para DTO usando ObjectMapper
        // (ou melhor: mudar o parâmetro para receber `EnderecoDTO` direto!)
        return ResponseEntity.ok("Endereço adicionado (mock)");
    }

    // 🚀 Método para definir endereço padrão
    public ResponseEntity<?> definirEnderecoPadrao(Long usuarioId, Long enderecoId) {
        return ResponseEntity.ok("Endereço padrão definido (mock)");
    }
}

