package com.example.usercrud.dtos;

import com.example.usercrud.model.ImagemProduto;
import com.example.usercrud.model.Produto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProdutoRespostaDTO {

    private long id;
    private String nomeProduto;
    private Double avaliacao;
    private String descricao;
    private BigDecimal preco;
    private int quantEstoque;
    private boolean ativo;
    private ImagensDTO imagemPrincipal;
    private List<ImagensDTO> imagens;

    public static ProdutoRespostaDTO fromEntity(Produto produto) {
        ProdutoRespostaDTO dto = new ProdutoRespostaDTO();
        dto.setId(produto.getId());
        dto.setNomeProduto(produto.getNomeProduto());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setAvaliacao(produto.getAvaliacao());
        dto.setQuantEstoque(produto.getQuantEstoque());
        dto.setAtivo(produto.isAtivo());

        if (produto.getImagens() != null) {
            produto.getImagens().stream()
                    .filter(ImagemProduto::isImagemPrincipal)
                    .findFirst()
                    .ifPresent(img -> dto.setImagemPrincipal(ImagensDTO.fromEntity(img)));
        }

        return dto;
    }

    public static ProdutoRespostaDTO getResponseComImagens(Produto produto) {
        ProdutoRespostaDTO dto = new ProdutoRespostaDTO();
        dto.setId(produto.getId());
        dto.setNomeProduto(produto.getNomeProduto());
        dto.setDescricao(produto.getDescricao());
        dto.setPreco(produto.getPreco());
        dto.setAvaliacao(produto.getAvaliacao());
        dto.setQuantEstoque(produto.getQuantEstoque());
        dto.setAtivo(produto.isAtivo());

        if (produto.getImagens() != null) {

            // converter todas as imagens em base64
            dto.setImagens(produto.getImagens().stream()
                    .map(ImagensDTO::fromEntity)
                    .collect(Collectors.toList()));

            produto.getImagens().stream()
                    .filter(ImagemProduto::isImagemPrincipal)
                    .findFirst()
                    .ifPresent(img -> dto.setImagemPrincipal(ImagensDTO.fromEntity(img)));
        }


        return dto;


    }
}


