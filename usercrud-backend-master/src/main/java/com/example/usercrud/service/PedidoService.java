package com.example.usercrud.service;

import com.example.usercrud.dtos.PedidoDetalhadoDTO;
import com.example.usercrud.enums.StatusPedido;
import com.example.usercrud.model.*;
import com.example.usercrud.repository.PedidoRepository;
import com.example.usercrud.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public Pedido criarPedido(Usuario usuario, Endereco endereco, List<ItemPedido> itensPedido, Pagamento pagamento) {
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setEnderecoEntrega(endereco);
        pedido.setPagamento(pagamento);
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido.setDataPedido(LocalDateTime.now());

        // Gerar número sequencial
        Long ultimoId = pedidoRepository.findTopByOrderByIdDesc()
                .map(Pedido::getId).orElse(0L);
        String numero = String.format("%06d", ultimoId + 1);
        pedido.setNumeroPedido(numero);

        // Calcular valor total
        BigDecimal total = itensPedido.stream()
                .map(item -> item.getPrecoUnitario().multiply(BigDecimal.valueOf(item.getQuantidadePedido())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        pedido.setItens(itensPedido);
        pedido.setValorTotal(total);

        return pedidoRepository.save(pedido);
    }

    public PedidoDetalhadoDTO detalharPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        return new PedidoDetalhadoDTO(pedido);
    }

    public List<PedidoDetalhadoDTO> listarPedidos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidos.stream().map(PedidoDetalhadoDTO::new).toList();
    }

    @Transactional
    public void alterarStatusPedido(Long id, StatusPedido status) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setStatus(status);

        pedidoRepository.save(pedido);
    }
}
