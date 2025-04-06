package com.example.usercrud.controller;

import com.example.usercrud.model.Carrinho;
import com.example.usercrud.service.CarrinhoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private CarrinhoService carrinhoService;

    @PostMapping("/adicionar/{id}")
    public ResponseEntity<Carrinho> adicionarProduto(@PathVariable Long id) {
        Carrinho carrinhoAtualizado = carrinhoService.adicionarProduto(id);
        return ResponseEntity.ok(carrinhoAtualizado);
    }

    @PutMapping("/aumentar/{id}")
    public ResponseEntity<Carrinho> aumentarQuantidade(@PathVariable Long id) {
        Carrinho carrinhoAtualizado = carrinhoService.aumentarQuantidade(id);
        return ResponseEntity.ok(carrinhoAtualizado);
    }

    @PutMapping("/diminuir/{id}")
    public ResponseEntity<Carrinho> diminuirQuantidade(@PathVariable Long id) {
        Carrinho carrinhoAtualizado = carrinhoService.diminuirQuantidade(id);
        return ResponseEntity.ok(carrinhoAtualizado);
    }

    @DeleteMapping("/remover/{id}")
    public ResponseEntity<Carrinho> removerProduto(@PathVariable Long id) {
        Carrinho carrinhoAtualizado = carrinhoService.removerProduto(id);
        return ResponseEntity.ok(carrinhoAtualizado);
    }

    @GetMapping("/total")
    public ResponseEntity<Double> calcularTotal() {
        double total = carrinhoService.calcularTotal();
        return ResponseEntity.ok(total);
    }
}