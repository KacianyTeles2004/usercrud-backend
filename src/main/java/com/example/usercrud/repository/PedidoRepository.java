package com.example.usercrud.repository;

import com.example.usercrud.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findTopByOrderByIdDesc();
}