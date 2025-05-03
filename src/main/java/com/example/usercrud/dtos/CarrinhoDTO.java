package com.example.usercrud.dtos;

import com.example.usercrud.dtos.ItemCarrinhoDTO;
import com.example.usercrud.model.Produto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CarrinhoDTO {
    private List<ItemCarrinhoDTO> itens = new ArrayList<>();

    public void adicionarItem(Produto produto, int quantidade) {
        for (ItemCarrinhoDTO item : itens) {
            if (item.getProduto().getId() == produto.getId()) {
                item.setQuantidade(item.getQuantidade() + quantidade);
                return;
            }
        }
        itens.add(new ItemCarrinhoDTO(produto, quantidade));
    }

    public void removerItem(long idProduto) {
        itens.removeIf(item -> item.getProduto().getId() == idProduto);
    }


    public BigDecimal getTotal() {
        return itens.stream()
                .map(ItemCarrinhoDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void limpar() {
        itens.clear();
    }
}

