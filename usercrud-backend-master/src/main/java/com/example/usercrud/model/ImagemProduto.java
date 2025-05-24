package com.example.usercrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Base64;

@Entity
@Table(name = "imagem-produtos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    // Métodos utilitários para base64
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
