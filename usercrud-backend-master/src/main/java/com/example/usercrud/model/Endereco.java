package com.example.usercrud.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "endereco") // Corrigi o nome da tabela para evitar problemas com acento
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cep;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;
    private boolean padrao;
}
