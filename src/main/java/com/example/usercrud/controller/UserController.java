package com.example.usercrud.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.usercrud.model.User;
import com.example.usercrud.repository.UserRepository;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/cadastrar")
    public ResponseEntity<String> cadastrar(@RequestBody User usuario) {
        if (userRepository.existsById(usuario.getCpf())) {
            return ResponseEntity.badRequest().body("{\"status\": \"cpf_ja_cadastrado\"}");
        }
        userRepository.save(usuario);
        return ResponseEntity.ok("{\"status\": \"usuario_cadastrado\"}");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String identificador, @RequestParam String senha) {
        User usuario = userRepository.findByCpfOrEmailAndSenha(identificador, senha);
        return usuario != null ? ResponseEntity.ok("{\"status\": \"sucesso\"}") : ResponseEntity.badRequest().body("{\"status\": \"falha\"}");
    }
}
