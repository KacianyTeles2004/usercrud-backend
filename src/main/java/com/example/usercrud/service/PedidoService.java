package com.example.usercrud.service;

import com.example.usercrud.dtos.CarrinhoDTO;
import com.example.usercrud.dtos.FinalizarCompraDTO;
import com.example.usercrud.dtos.ItemCarrinhoDTO;
import com.example.usercrud.model.*;
import com.example.usercrud.repository.PedidoRepository;
import com.example.usercrud.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    @Transactional
    public Pedido finalizarPedido(Usuario cliente, CarrinhoDTO carrinho, FinalizarCompraDTO dto) {
        if (carrinho == null || carrinho.getItens().isEmpty()) {
            throw new RuntimeException("Carrinho está vazio");
        }

        // Criar pedido
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setEnderecoEntrega(dto.getEnderecoEntrega());
        pedido.setFormaPagamento(dto.getFormaPagamento());
        pedido.setStatus("aguardando pagamento");

        // Gerar número sequencial
        Long ultimoId = pedidoRepository.findTopByOrderByIdDesc()
                .map(Pedido::getId).orElse(0L);
        String numero = String.format("%06d", ultimoId + 1);
        pedido.setNumeroPedido(numero);

        // Criar itens do pedido
        List<ItemPedido> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (ItemCarrinhoDTO item : carrinho.getItens()) {
            Produto produto = produtoRepository.findById(item.getProduto().getId())
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            ItemPedido itemPedido = new ItemPedido();
            itemPedido.setProdutoPedido(produto);
            itemPedido.setQuantidadePedido(item.getQuantidade());
            itemPedido.setPrecoUnitario(produto.getPreco());
            itemPedido.setPedido(pedido);

            itens.add(itemPedido);

            total = total.add(produto.getPreco()
                    .multiply(BigDecimal.valueOf(item.getQuantidade())));
        }

        pedido.setItens(itens);
        pedido.setValorTotal(total);

        return pedidoRepository.save(pedido);
    }
}
