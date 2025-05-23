package com.example.usercrud.dtos;

import com.example.usercrud.model.Endereco;
import lombok.Data;

@Data
public class EnderecoDTO {
    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private boolean padrao;

    // Construtor que transforma Endereco em EnderecoDTO
    public EnderecoDTO(Endereco endereco) {
        if (endereco == null) {
            return; // ou lance uma exceção, conforme sua necessidade
        }
        this.cep = endereco.getCep();
        this.logradouro = endereco.getLogradouro();
        this.numero = endereco.getNumero();
        this.complemento = endereco.getComplemento();
        this.bairro = endereco.getBairro();
        this.cidade = endereco.getCidade();
        this.uf = endereco.getUf();
        this.padrao = endereco.isPadrao();
    }
}

