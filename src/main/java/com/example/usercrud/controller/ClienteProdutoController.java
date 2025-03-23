package com.example.usercrud.controller;

import com.example.usercrud.service.ProdutoService;
import com.example.usercrud.service.CarrinhoService;
import com.example.usercrud.service.FreteService;
import com.example.usercrud.model.Produto;
import com.example.usercrud.model.Carrinho;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente/produto")
public class ClienteProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private FreteService freteService;

    @GetMapping("/listar")
    public List<Produto> listarProdutos() {
        return produtoService.listarProdutos();
    }

    @GetMapping("/{id}")
    public Produto visualizarProduto(@PathVariable Long id) {
        return produtoService.buscarProduto(id);
    }

    @PostMapping("/carrinho/adicionar/{id}")
    public Carrinho adicionarProdutoCarrinho(@PathVariable Long id) {
        return carrinhoService.adicionarProduto(id);
    }

    @PostMapping("/carrinho/aumentar/{id}")
    public Carrinho aumentarQuantidade(@PathVariable Long id) {
        return carrinhoService.aumentarQuantidade(id);
    }

    @PostMapping("/carrinho/diminuir/{id}")
    public Carrinho diminuirQuantidade(@PathVariable Long id) {
        return carrinhoService.diminuirQuantidade(id);
    }

    @DeleteMapping("/carrinho/remover/{id}")
    public Carrinho removerProdutoCarrinho(@PathVariable Long id) {
        return carrinhoService.removerProduto(id);
    }

    @GetMapping("/frete/{cep}")
    public double calcularFrete(@PathVariable String cep) {
        return freteService.calcularFrete(cep);
    }
}

