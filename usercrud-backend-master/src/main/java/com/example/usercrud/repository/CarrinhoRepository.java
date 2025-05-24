package com.example.usercrud.repository;

import com.example.usercrud.model.Carrinho;
import com.example.usercrud.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CarrinhoRepository extends JpaRepository<Carrinho, Long> {
    Optional<Carrinho> findByUsuarioAndFinalizadoFalse(Usuario usuario);
}

