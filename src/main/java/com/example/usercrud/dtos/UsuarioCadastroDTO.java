package com.example.usercrud.dtos;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
public class UsuarioCadastroDTO {
    private String name;
    @CPF(message = "CPF invalido")
    private String cpf;
    private String email;
    private String senha;
    private Boolean ativo;
    private String tipo;
}
