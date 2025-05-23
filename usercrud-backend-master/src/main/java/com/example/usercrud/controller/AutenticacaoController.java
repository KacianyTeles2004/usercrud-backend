package com.example.usercrud.controller;

import com.example.usercrud.dtos.UsuarioRespostaDTO;
import com.example.usercrud.enums.TiposUsuarios;
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

    // DTO de autenticação
    public record AutenticacaoUsuarioDTO(String email, String senha) {}

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody @Valid AutenticacaoUsuarioDTO request) {
        try {
            var usuario = usuarioRepository.findByEmail(request.email()).orElse(null);

            if (usuario == null) {
                return ResponseEntity.status(404).body("Usuário não encontrado");
            }

            if (!usuario.getAtivo()) {
                return ResponseEntity.status(403).body("A conta está desativada");
            }

            if (usuario.getTipo() == null ||
                    (usuario.getTipo() != TiposUsuarios.ADMIN && usuario.getTipo() != TiposUsuarios.STOCKIST)) {
                return ResponseEntity.status(403).body("Usuário não autorizado");
            }

            boolean senhaCorreta = codificadorDeSenha.matches(request.senha(), usuario.getSenha());

            if (!senhaCorreta) {
                return ResponseEntity.status(401).body("Email ou senha inválidos");
            }

            return ResponseEntity.ok(new UsuarioRespostaDTO(
                    usuario.getId(),
                    usuario.getNomeCompleto(),
                    usuario.getTipo()
            ));

        } catch (Exception e) {
            log.error("Erro ao autenticar usuário", e);
            return ResponseEntity.status(500).body("Erro interno. Tente novamente mais tarde.");
        }
    }
}
