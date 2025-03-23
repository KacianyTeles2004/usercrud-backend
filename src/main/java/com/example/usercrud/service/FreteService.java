package com.example.usercrud.service;

import org.springframework.stereotype.Service;

@Service
public class FreteService {

    public double calcularFrete(String cep) {
        return 20.0; // Valor exemplo
    }
}
