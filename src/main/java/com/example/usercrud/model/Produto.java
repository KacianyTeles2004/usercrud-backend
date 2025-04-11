package com.example.usercrud.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Produto {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 200)
    private String nomeProduto;

    @Column
    private Double avaliacao;

    @Column(length = 2000)
    private String descricao;

    @Column
    private BigDecimal preco;

    @Column
    private int quantEstoque;

    @Column
    private boolean ativo;

    @OneToMany(mappedBy = "produto", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImagemProduto> imagens;


    public void  alterarStatus() {
        this.ativo = !ativo;
    }
}
