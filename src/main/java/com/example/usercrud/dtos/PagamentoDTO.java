package com.example.usercrud.dtos;

import lombok.Data;

@Data
public class PagamentoDTO {
    private String formaPagamento;        // "boleto" ou "cartao"

    // Campos para cartão
    private String numeroCartao;
    private String codigoVerificador;
    private String nomeTitular;
    private String dataValidade;
    private int parcelas;
}

