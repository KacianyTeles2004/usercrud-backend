package com.example.usercrud.model;

import com.example.usercrud.enums.TiposUsuarios;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String nomeCompleto;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    @CPF(message = "O CPF informado é inválido")
    private String cpf;

    @Enumerated(EnumType.STRING)
    private TiposUsuarios tipo;

    @Column(nullable = false)
    private Boolean ativo = true; // Pode iniciar como true se preferir

    @Column(nullable = false)
    private String senha;

    private String dataNascimento;

    private String genero;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_faturamento_id")
    private Endereco enderecoFaturamento;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_id")
    private List<Endereco> enderecosEntrega;
}
