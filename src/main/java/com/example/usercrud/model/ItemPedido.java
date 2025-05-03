package com.example.usercrud.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_pedido")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ItemPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Produto produtoPedido;

    @Column
    private int quantidadePedido;

    @Column
    private BigDecimal precoUnitario;

    @ManyToOne
    private Pedido pedido;
}