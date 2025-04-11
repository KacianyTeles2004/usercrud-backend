package com.example.usercrud.controller;


import com.example.usercrud.dtos.AutenticacaoUsuarioDTO;
import com.example.usercrud.dtos.UsuarioRespostaDTO;
import com.example.usercrud.model.TiposUsuarios;
import com.example.usercrud.repository.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@CrossOrigin("*")
@Slf4j
public class AutenticacaoController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder codificadorDeSenha;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AutenticacaoUsuarioDTO request) {
        try {
            var usuario = usuarioRepository.findByEmail(request.email()).orElse(null);

            if (usuario == null) {
                return ResponseEntity.status(404).body("Usuário não encontrado");
            }
            if (usuario.getTipo() != TiposUsuarios.ADMIN && usuario.getTipo() != TiposUsuarios.STOCKIST) {
                return ResponseEntity.status(401).body("Usuário não autorizado");
            }
            if (!usuario.getAtivo()) {
                return ResponseEntity.status(400).body("A conta deste usuário está desativada");
            }

            if (codificadorDeSenha.matches(request.senha(), usuario.getSenha()) && usuario.getEmail().equals(request.email())) {

                return ResponseEntity.ok().body(new UsuarioRespostaDTO(usuario.getId(), usuario.getName(), usuario.getTipo()));

            } else {
                return ResponseEntity.status(400).body("Email ou senha inválido");
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Ops, tivemos um erro interno, tente novamente por favor :)");
        }
    }


}

