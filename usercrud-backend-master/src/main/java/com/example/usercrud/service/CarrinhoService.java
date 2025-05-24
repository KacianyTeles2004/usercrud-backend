package com.example.usercrud.service;

import com.example.usercrud.model.*;
import com.example.usercrud.repository.CarrinhoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarrinhoService {

    private final CarrinhoRepository carrinhoRepository;

    public Carrinho buscarOuCriarCarrinho(Usuario usuario) {
        return carrinhoRepository.findByUsuarioAndFinalizadoFalse(usuario)
                .orElseGet(() -> {
                    Carrinho novo = Carrinho.builder()
                            .usuario(usuario)
                            .finalizado(false)
                            .build();
                    return carrinhoRepository.save(novo);
                });
    }

    public Carrinho salvar(Carrinho carrinho) {
        return carrinhoRepository.save(carrinho);
    }

    public void finalizarCarrinho(Carrinho carrinho) {
        carrinho.setFinalizado(true);
        carrinhoRepository.save(carrinho);
    }
}
