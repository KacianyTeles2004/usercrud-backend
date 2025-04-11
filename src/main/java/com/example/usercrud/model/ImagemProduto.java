package com.example.usercrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;

@Entity
@Table(name = "imagem-produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ImagemProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String URL;

    @Column
    private boolean imagemPrincipal;

    @ManyToOne
    @JsonIgnore
    private Produto produto;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] conteudo;


    @Transient
    public void setConteudoBase64(String conteudoBase64) {
        if (conteudoBase64 != null && !conteudoBase64.isEmpty()) {
            this.conteudo = Base64.getDecoder().decode(conteudoBase64);
        }
    }

    @Transient
    public String getConteudoBase64() {
        if (conteudo != null) {
            return Base64.getEncoder().encodeToString(conteudo);
        }
        return null;
    }
}
