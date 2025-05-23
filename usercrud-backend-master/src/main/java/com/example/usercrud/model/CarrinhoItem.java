package com.example.usercrud.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carrinho_itens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarrinhoItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Carrinho carrinho;

    @ManyToOne
    private Produto produto;

    private int quantidade;
}
