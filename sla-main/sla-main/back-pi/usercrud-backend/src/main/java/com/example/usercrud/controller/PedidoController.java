package com.example.usercrud.controller;

import com.example.usercrud.model.Pedido;
import com.example.usercrud.model.Usuario;
import com.example.usercrud.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @GetMapping
    public List<Pedido> listarPedidos(@RequestAttribute("usuario") Usuario usuario) {
        return pedidoService.listarPedidosDoUsuario(usuario);
    }
}
