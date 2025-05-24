package com.example.usercrud.dtos;

import com.example.usercrud.model.Endereco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioCadastroDTO {
    private String nomeCompleto;
    private String email;
    private String cpf;
    private String senha;
    private String dataNascimento;
    private String genero;
    private Endereco enderecoFaturamento;
    private List<Endereco> enderecosEntrega;
}