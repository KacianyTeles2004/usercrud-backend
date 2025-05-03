package com.example.usercrud.controller;

import com.example.usercrud.dtos.CarrinhoDTO;
import com.example.usercrud.model.*;
import com.example.usercrud.repository.ProdutoRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
@RequiredArgsConstructor
public class CarrinhoController {

    private final ProdutoRepository produtoRepo;

    @PostMapping("/adicionar")
    public ResponseEntity<?> adicionarProduto (@RequestParam long idProduto,
                                               @RequestParam int quantidade, HttpSession session) {
        CarrinhoDTO carrinho = (CarrinhoDTO) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new CarrinhoDTO();
        }

        Produto produto = produtoRepo.findById(idProduto).orElse(null);
        if (produto == null) return ResponseEntity.badRequest().body("Produto não encontrado");

        carrinho.adicionarItem(produto, quantidade);
        session.setAttribute("carrinho", carrinho);
        return ResponseEntity.ok("Produto adicionado ao carrinho.");
    }

    @PostMapping("/finalizar")
    public ResponseEntity<?> finalizarCompra(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return ResponseEntity.status(302).header("Location", "/login").build();
        }
        return ResponseEntity.status(302).header("Location", "/checkout/inicio").build();
    }

    @GetMapping
    public ResponseEntity<?> visualizarCarrinho(HttpSession session) {
        CarrinhoDTO carrinho = (CarrinhoDTO) session.getAttribute("carrinho");
        if (carrinho == null || carrinho.getItens().isEmpty()) {
            return ResponseEntity.ok("Carrinho vazio");
        }
        return ResponseEntity.ok(carrinho);
    }
}
