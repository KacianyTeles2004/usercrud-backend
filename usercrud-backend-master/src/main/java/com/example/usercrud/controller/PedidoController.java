package com.example.usercrud.controller;

import com.example.usercrud.dtos.PedidoDetalhadoDTO;
import com.example.usercrud.enums.StatusPedido;
import com.example.usercrud.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // 🔍 Listar todos os pedidos
    @GetMapping
    public ResponseEntity<List<PedidoDetalhadoDTO>> listarPedidos() {
        List<PedidoDetalhadoDTO> pedidos = pedidoService.listarPedidos();
        return ResponseEntity.ok(pedidos);
    }

    // 🔍 Detalhar um pedido pelo ID
    @GetMapping("/{id}")
    public ResponseEntity<PedidoDetalhadoDTO> detalharPedido(@PathVariable Long id) {
        PedidoDetalhadoDTO pedido = pedidoService.detalharPedido(id);
        return ResponseEntity.ok(pedido);
    }

    // 🔄 Alterar status do pedido
    @PutMapping("/{id}/status")
    public ResponseEntity<Void> alterarStatus(
            @PathVariable Long id,
            @RequestParam StatusPedido status
    ) {
        pedidoService.alterarStatusPedido(id, status);
        return ResponseEntity.ok().build();
    }
}

