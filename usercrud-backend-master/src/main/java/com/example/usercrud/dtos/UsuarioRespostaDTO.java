package com.example.usercrud.dtos;


import com.example.usercrud.enums.TiposUsuarios;
import com.example.usercrud.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRespostaDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String cpf;

    public UsuarioRespostaDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.name = usuario.getNomeCompleto();
        this.email = usuario.getEmail();
        this.cpf = usuario.getCpf();
        this.role = usuario.getTipo().toString();
    }

    public UsuarioRespostaDTO(Long id, String name, TiposUsuarios role) {
        this.id = id;
        this.name = name;
        this.role = role.toString();
    }
}
