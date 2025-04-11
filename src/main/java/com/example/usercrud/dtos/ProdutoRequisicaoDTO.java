package com.example.usercrud.dtos;

import com.example.usercrud.model.ImagemProduto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProdutoRequisicaoDTO {

    private long id;
    private String nomeProduto;
    private Double avaliacao;
    private String descricao;
    private BigDecimal preco;
    private int quantEstoque;
    private boolean ativo;
    private List<ImagemProduto> imagens;


}
