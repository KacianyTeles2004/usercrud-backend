package com.example.usercrud.service;

import com.example.usercrud.dtos.AutenticacaoUsuarioDTO;
import com.example.usercrud.model.Usuario;
import com.example.usercrud.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AutenticacaoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public ResponseEntity<?> autenticar(AutenticacaoUsuarioDTO autenticacaoUsuarioDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByEmail(autenticacaoUsuarioDTO.getEmail());

        if (usuarioOptional.isPresent() &&
                passwordEncoder.matches(autenticacaoUsuarioDTO.getSenha(), usuarioOptional.get().getSenha())) {

            return ResponseEntity.ok("Login realizado com sucesso!");
        }

        return ResponseEntity.badRequest().body("Usuário ou senha inválidos.");
    }

    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logout realizado com sucesso!");
    }
}
