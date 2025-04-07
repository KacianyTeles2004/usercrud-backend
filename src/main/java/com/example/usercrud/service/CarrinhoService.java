package com.example.usercrud.service;

import org.springframework.stereotype.Service;

import com.example.usercrud.model.Carrinho;
import com.example.usercrud.model.Produto;

@Service
public class CarrinhoService {

    private Carrinho carrinho = new Carrinho();

    public Carrinho adicionarProduto(Long id) {
        Produto produto = new Produto(); // Create or retrieve a Produto object
        carrinho.adicionarProduto(produto);
        return carrinho;
    }

    public Carrinho aumentarQuantidade(Long id) {
        carrinho.aumentarQuantidade(id);
        return carrinho;
    }

    public Carrinho diminuirQuantidade(Long id) {
        carrinho.diminuirQuantidade(id);
        return carrinho;
    }

    public Carrinho removerProduto(Long id) {
        carrinho.removerProduto(id);
        return carrinho;
    }
}
