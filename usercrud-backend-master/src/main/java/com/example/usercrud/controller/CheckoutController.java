package com.example.usercrud.controller;

import com.example.usercrud.dtos.FinalizarCompraDTO;
import com.example.usercrud.dtos.FinalizarCompraDTO;
import com.example.usercrud.model.*;
import com.example.usercrud.service.CarrinhoService;
import com.example.usercrud.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CarrinhoService carrinhoService;
    private final PedidoService pedidoService;

    @PostMapping("/finalizar")
    public Pedido finalizarCompra(
            @RequestBody FinalizarCompraDTO dados,
            @RequestAttribute("usuario") Usuario usuario) {

        Carrinho carrinho = carrinhoService.buscarOuCriarCarrinho(usuario);
        List<ItemPedido> itensPedido = dados.getItens();
        Pagamento pagamento = dados.getPagamento();

        Pedido pedido = pedidoService.criarPedido(
                usuario,
                dados.getEnderecoEntrega(),
                itensPedido,
                pagamento
        );

        carrinhoService.finalizarCarrinho(carrinho);
        return pedido;
    }
}
