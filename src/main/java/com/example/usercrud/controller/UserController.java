package com.example.usercrud.controller;

import com.example.usercrud.dtos.UsuarioCadastroDTO;
import com.example.usercrud.dtos.UsuarioRespostaDTO;
import com.example.usercrud.model.Usuario;
import com.example.usercrud.repository.UsuarioRepository;
import com.example.usercrud.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuario")
@CrossOrigin("*")
public class UserController {

    private final UsuarioService service;
    private final UsuarioRepository repository;

    public UserController(UsuarioRepository repository, UsuarioService service) {
        this.repository = repository;

        this.service = service;
    }


    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrarUsuario(@RequestBody UsuarioCadastroDTO requestDTO) {
        try {
            var usuarioSalvo = service.cadastrar(requestDTO);

            // ResponseDTO com os dados necessarios
            return ResponseEntity.ok(new UsuarioRespostaDTO(usuarioSalvo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao salvar usuário: " + e.getMessage());
        }
    }

    @GetMapping("/listarTodos")
    public ResponseEntity<List<Usuario>> listarUsuarios() {
        return ResponseEntity.ok(repository.findAll());
    }

    // se ta presente eu devolvo os dados dele
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/editar/{id}")
    public ResponseEntity<Object> editarUsuario(@PathVariable Long id, @RequestBody UsuarioCadastroDTO requestDTO) {
        try {
            var usuarioSalvo = service.editar(id, requestDTO);
            return ResponseEntity.ok(new UsuarioRespostaDTO(usuarioSalvo));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao editar usuário: " + e.getMessage());
        }
    }
}

