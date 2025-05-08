package com.example.usercrud.service;

import com.example.usercrud.enums.StatusPedido;
import com.example.usercrud.model.*;
import com.example.usercrud.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;

    public Pedido criarPedido(Usuario usuario, Endereco endereco, List<PedidoItem> itens, Pagamento pagamento) {
        BigDecimal valorTotal = itens.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Pedido pedido = Pedido.builder()
                .usuario(usuario)
                .enderecoEntrega(endereco)
                .dataPedido(LocalDateTime.now())
                .numeroPedido(UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .status(StatusPedido.AGUARDANDO_PAGAMENTO)
                .itens(itens)
                .valorTotal(valorTotal)
                .pagamento(pagamento)
                .build();

        itens.forEach(item -> item.setPedido(pedido));
        return pedidoRepository.save(pedido);
    }

    public List<Pedido> listarPedidosDoUsuario(Usuario usuario) {
        return pedidoRepository.findByUsuario(usuario);
    }
}
