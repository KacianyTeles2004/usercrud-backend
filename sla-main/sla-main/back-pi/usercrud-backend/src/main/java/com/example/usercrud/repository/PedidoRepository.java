package com.example.usercrud.repository;

import com.example.usercrud.model.Pedido;
import com.example.usercrud.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByUsuario(Usuario usuario);
}
