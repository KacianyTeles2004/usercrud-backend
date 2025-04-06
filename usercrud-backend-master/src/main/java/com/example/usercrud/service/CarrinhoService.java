package com.example.usercrud.service;

import com.example.usercrud.model.Carrinho;
import com.example.usercrud.model.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

@Service
public class CarrinhoService {

    private Carrinho carrinho = new Carrinho();

    @Autowired
    private ProdutoService produtoService;

    public Carrinho adicionarProduto(Long id) {
        Produto produto = produtoService.buscarProdutoPorId(id);
        if (produto != null) {
            carrinho.adicionarProduto(produto);
        }
        return carrinho;
    }

    public Carrinho aumentarQuantidade(Long id) {
        Produto produto = produtoService.buscarProdutoPorId(id);
        if (produto != null) {
            carrinho.aumentarQuantidade(produto);
        }
        return carrinho;
    }

    public Carrinho diminuirQuantidade(Long id) {
        Produto produto = produtoService.buscarProdutoPorId(id);
        if (produto != null) {
            carrinho.diminuirQuantidade(produto);
        }
        return carrinho;
    }

    public Carrinho removerProduto(Long id) {
        Produto produto = produtoService.buscarProdutoPorId(id);
        if (produto != null) {
            carrinho.removerProduto(produto);
        }
        return carrinho;
    }

    public double calcularTotal() {
        return carrinho.calcularTotal();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Produto> buscarProdutoPorId(@PathVariable Long id) {
        Produto produto = produtoService.buscarProdutoPorId(id);
        if (produto != null) {
            return ResponseEntity.ok(produto);
        }
        return ResponseEntity.notFound().build();
    }
}
