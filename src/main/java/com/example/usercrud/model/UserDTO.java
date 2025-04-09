package com.example.usercrud.model;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, message = "Nome deve conter no mínimo 3 letras")
    private String nomeCompleto;

    @Email(message = "Email inválido")
    private String email;

    @NotBlank
    @Size(min = 11, max = 11, message = "CPF deve conter 11 dígitos")
    private String cpf;

    @NotBlank
    private String dataNascimento;

    @NotBlank
    private String genero;

    @NotBlank
    private String senha;

    @NotBlank
    private String cep;

    @NotBlank
    private String logradouro;

    @NotBlank
    private String numero;

    private String complemento;

    @NotBlank
    private String bairro;

    @NotBlank
    private String cidade;

    @NotBlank
    private String uf;

    private List<String> enderecosEntrega;


    private boolean enderecoEntregaIgual;


}
