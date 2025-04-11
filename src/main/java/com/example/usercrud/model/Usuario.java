package com.example.usercrud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Entity
@Table(name = "usuarios")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    @CPF(message = "O cpf informado é inválido")
    private String cpf;

    @Enumerated(EnumType.STRING)
    private TiposUsuarios tipo;

    private Boolean ativo;

    private String senha;

}