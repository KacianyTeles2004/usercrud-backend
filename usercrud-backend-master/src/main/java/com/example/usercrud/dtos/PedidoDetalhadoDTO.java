package com.example.usercrud.dtos;

import com.example.usercrud.enums.FormaPagamento;
import com.example.usercrud.enums.StatusPedido;
import com.example.usercrud.model.Pedido;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class PedidoDetalhadoDTO {
    private String numeroPedido;
    private LocalDateTime dataPedido;
    private StatusPedido status;
    private BigDecimal valorTotal;
    private List<ItemPedidoDTO> itens;
    private EnderecoDTO endereco;
    private FormaPagamento formaPagamento;

    public PedidoDetalhadoDTO(Pedido pedido) {
        this.numeroPedido = pedido.getNumeroPedido();
        this.dataPedido = pedido.getDataPedido();
        this.status = pedido.getStatus();
        this.valorTotal = pedido.getValorTotal();
        this.formaPagamento = pedido.getPagamento().getForma();
        this.itens = pedido.getItens().stream()
                .map(ItemPedidoDTO::new)
                .toList();
        this.endereco = new EnderecoDTO(pedido.getEndereco());
    }
}
