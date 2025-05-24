package com.example.usercrud.dtos;

import com.example.usercrud.model.ItemPedido;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ItemPedidoDTO {
    private String nomeProduto;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;

    public ItemPedidoDTO(ItemPedido item) {
        this.nomeProduto = item.getProduto().getNomeProduto();
        this.quantidade = item.getQuantidadePedido();
        this.precoUnitario = item.getPrecoUnitario();
        this.subtotal = item.getPrecoUnitario().multiply(BigDecimal.valueOf(quantidade));
    }
}
