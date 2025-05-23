package com.example.usercrud.dtos;

import com.example.usercrud.enums.FormaPagamento;
import com.example.usercrud.model.Endereco;
import com.example.usercrud.model.ItemPedido;
import com.example.usercrud.model.Pagamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FinalizarCompraDTO {
    private Endereco enderecoEntrega;
    private FormaPagamento formaPagamento;
    private List<ItemPedido> itens;
    private Pagamento pagamento;
}
