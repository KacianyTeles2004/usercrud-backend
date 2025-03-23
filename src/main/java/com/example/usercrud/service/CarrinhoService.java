package com.example.usercrud.service;

import com.example.usercrud.model.Produto;
import com.example.usercrud.model.Carrinho;
import org.springframework.stereotype.Service;

@Service
public class CarrinhoService {

    private Carrinho carrinho = new Carrinho();

    public Carrinho adicionarProduto(Long id) {
        Produto produto =
                carrinho.adicionarProduto(Produto);
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
