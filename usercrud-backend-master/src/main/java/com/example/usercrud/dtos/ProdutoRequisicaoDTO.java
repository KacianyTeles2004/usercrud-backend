package com.example.usercrud.dtos;

import com.example.usercrud.model.ImagemProduto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoRequisicaoDTO {

    private Long id;
    private Long usuarioId;


    @NotBlank(message = "O nome do produto é obrigatório.")
    private String nomeProduto;

    @DecimalMin(value = "0.0", inclusive = true, message = "A avaliação deve ser no mínimo 0.")
    @DecimalMax(value = "5.0", inclusive = true, message = "A avaliação deve ser no máximo 5.")
    private Double avaliacao;

    @NotBlank(message = "A descrição é obrigatória.")
    private String descricao;

    @NotNull(message = "O preço é obrigatório.")
    @DecimalMin(value = "0.0", inclusive = false, message = "O preço deve ser maior que 0.")
    private BigDecimal preco;

    @Min(value = 0, message = "A quantidade em estoque não pode ser negativa.")
    private int quantEstoque;

    private boolean ativo;

    @Valid
    private List<ImagemProduto> imagens;
}
