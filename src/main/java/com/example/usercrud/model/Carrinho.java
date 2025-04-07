package com.example.usercrud.model;

import java.util.HashMap;
import java.util.Map;

public class Carrinho {
    private final Map<Produto, Integer> itens = new HashMap<>();
    private double frete;

    public void adicionarProduto(Produto produto) {
        itens.put(produto, itens.getOrDefault(produto, 0) + 1);
    }

    public void aumentarQuantidade(Long id) {
        Produto produto = itens.keySet().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (produto != null) {
            itens.put(produto, itens.get(produto) + 1);
        }
    }

    public void diminuirQuantidade(Long id) {
        Produto produto = itens.keySet().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (produto != null) {
            if (itens.get(produto) > 1) {
                itens.put(produto, itens.get(produto) - 1);
            } else {
                itens.remove(produto);
            }
        }
    }

    public void removerProduto(Long id) {
        Produto produto = itens.keySet().stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (produto != null) {
            itens.remove(produto);
        }
    }

    public double calcularTotal() {
        return itens.entrySet().stream()
                .mapToDouble(entry -> entry.getKey().getPreco() * entry.getValue())
                .sum() + frete;
    }
}