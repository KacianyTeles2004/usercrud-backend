package com.example.usercrud.dtos;

import com.example.usercrud.model.Endereco;
import com.example.usercrud.model.Pagamento;
import com.example.usercrud.model.PedidoItem;
import lombok.Data;

import java.util.List;

@Data
public class FinalizarPedidoDTO {
    private Endereco enderecoEntrega;
    private Pagamento pagamento;
    private List<PedidoItem> itens;
}
