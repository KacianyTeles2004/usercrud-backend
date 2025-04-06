package com.example.usercrud.model;

import java.util.HashMap;
import java.util.Map;

public class Carrinho {
    private Map<Produto, Integer> itens = new HashMap<>();
    private double frete;

    public void adicionarProduto(Produto produto) {
        itens.put(produto, itens.getOrDefault(produto, 0) + 1);
    }

    public void aumentarQuantidade(Produto produto) {
        if (itens.containsKey(produto)) {
            itens.put(produto, itens.get(produto) + 1);
        }
    }

    public void diminuirQuantidade(Produto produto) {
        if (itens.containsKey(produto)) {
            if (itens.get(produto) > 1) {
                itens.put(produto, itens.get(produto) - 1);
            } else {
                itens.remove(produto);
            }
        }
    }

    public void removerProduto(Produto produto) {
        itens.remove(produto);
    }

    public double calcularTotal() {
        return itens.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPreco() * entry.getValue())
                .sum() + frete;
    }

    public Map<Produto, Integer> getItens() {
        return itens;
    }

    public double getFrete() {
        return frete;
    }

    public void setFrete(double frete) {
        this.frete = frete;
    }
}