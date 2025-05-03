package com.example.usercrud.controller;

import com.example.usercrud.dtos.CarrinhoDTO;
import com.example.usercrud.dtos.FinalizarCompraDTO;
import com.example.usercrud.dtos.ItemCarrinhoDTO;
import com.example.usercrud.dtos.PagamentoDTO;
import com.example.usercrud.model.Usuario;
import com.example.usercrud.model.Pedido;
import com.example.usercrud.service.PedidoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/checkout")
public class CheckoutController {

    private final PedidoService pedidoService;

    public CheckoutController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping("/resumo")
    public ResponseEntity<?> getResumoPedido(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        CarrinhoDTO carrinho = (CarrinhoDTO) session.getAttribute("carrinho");
        FinalizarCompraDTO dadosEntrega = (FinalizarCompraDTO) session.getAttribute("dadosEntrega");

        if (usuario == null || carrinho == null || dadosEntrega == null) {
            return ResponseEntity.badRequest().body("Sessão inválida");
        }

        BigDecimal frete = new BigDecimal("25.00"); // Exemplo fixo

        BigDecimal totalProdutos = carrinho.getTotal();
        BigDecimal totalGeral = totalProdutos.add(frete);

        var resumo = new Object() {
            public final List<ItemCarrinhoDTO> itens = carrinho.getItens();
            public final BigDecimal totalProdutosCarrinho = totalProdutos;
            public final BigDecimal fretePedido = frete;
            public final BigDecimal totalPedido = totalGeral;
            public final String enderecoEntrega = dadosEntrega.getEnderecoEntrega();
            public final String formaPagamento = dadosEntrega.getFormaPagamento();
            public final String nomeCliente = usuario.getName();
        };

        return ResponseEntity.ok(resumo);
    }

    @PostMapping("/pagamento")
    public ResponseEntity<?> escolherFormaPagamento(@RequestBody PagamentoDTO pagamentoDTO, HttpSession session) {
        if (pagamentoDTO.getFormaPagamento() == null || pagamentoDTO.getFormaPagamento().isBlank()) {
            return ResponseEntity.badRequest().body("Forma de pagamento obrigatória");
        }

        if (pagamentoDTO.getFormaPagamento().equalsIgnoreCase("cartao")) {
            if (pagamentoDTO.getNumeroCartao() == null || pagamentoDTO.getCodigoVerificador() == null ||
                    pagamentoDTO.getDataValidade() == null || pagamentoDTO.getNomeTitular() == null ||
                    pagamentoDTO.getParcelas() <= 0) {
                return ResponseEntity.badRequest().body("Dados do cartão incompletos");
            }
        }

        session.setAttribute("dadosPagamento", pagamentoDTO);

        return ResponseEntity.ok("Forma de pagamento registrada com sucesso");
    }


    @PostMapping("/concluir")
    public ResponseEntity<?> concluirCompra(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        CarrinhoDTO carrinho = (CarrinhoDTO) session.getAttribute("carrinho");
        FinalizarCompraDTO dto = (FinalizarCompraDTO) session.getAttribute("dadosEntrega");

        if (usuario == null || carrinho == null || dto == null) {
            return ResponseEntity.badRequest().body("Sessão inválida");
        }

        Pedido pedido = pedidoService.finalizarPedido(usuario, carrinho, dto);

        session.removeAttribute("carrinho");

        return ResponseEntity.ok("Pedido finalizado com sucesso! Número: " + pedido.getNumeroPedido());
    }

}