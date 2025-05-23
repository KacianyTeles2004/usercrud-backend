package com.example.usercrud.controller;

import com.example.usercrud.dtos.UsuarioCadastroDTO;
import com.example.usercrud.dtos.AutenticacaoUsuarioDTO;
import com.example.usercrud.dtos.UsuarioRespostaDTO;
import com.example.usercrud.model.Usuario;
import com.example.usercrud.service.UsuarioService;
import com.example.usercrud.service.AutenticacaoService;
import com.example.usercrud.service.EnderecoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UserController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AutenticacaoService autenticacaoService;

    @Autowired
    private EnderecoService enderecoService;

    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrarUsuario(@Valid @RequestBody UsuarioCadastroDTO usuarioCadastroDTO) {
        return usuarioService.cadastrar(usuarioCadastroDTO);
    }

    @PutMapping("/atualizar/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable Long id, @Valid @RequestBody UsuarioCadastroDTO usuarioCadastroDTO) {
        try {
            Usuario atualizado = usuarioService.editar(id, usuarioCadastroDTO);
            return ResponseEntity.ok().body(new UsuarioRespostaDTO(atualizado));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar usuário: " + e.getMessage());
        }
    }


    @PostMapping("/login")
    public ResponseEntity<?> autenticarUsuario(@Valid @RequestBody AutenticacaoUsuarioDTO autenticacaoUsuarioDTO) {
        return autenticacaoService.autenticar(autenticacaoUsuarioDTO);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUsuario() {
        return autenticacaoService.logout();
    }

    @PostMapping("/endereco/adicionar")
    public ResponseEntity<?> adicionarEndereco(@RequestParam Long usuarioId, @RequestBody String enderecoJson) {
        return enderecoService.adicionarEndereco(usuarioId, enderecoJson);
    }

    @PutMapping("/endereco/definir-padrao")
    public ResponseEntity<?> definirEnderecoPadrao(@RequestParam Long usuarioId, @RequestParam Long enderecoId) {
        return enderecoService.definirEnderecoPadrao(usuarioId, enderecoId);
    }
}