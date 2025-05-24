package com.example.usercrud.dtos;

import com.example.usercrud.model.ImagemProduto;
import lombok.Data;

import java.util.Base64;

@Data
public class ImagensDTO {
    private Long id;
    private String URL;
    private boolean imagemPrincipal;
    private String conteudo; //  em Base64

    public static ImagensDTO fromEntity(ImagemProduto entity) {
        ImagensDTO dto = new ImagensDTO();
        dto.setId(entity.getId());
        dto.setURL(entity.getURL());
        dto.setImagemPrincipal(entity.isImagemPrincipal());

        // converte o binario em base64 pro frnt conseguir mostrar
        if (entity.getConteudo() != null) {
            dto.setConteudo(Base64.getEncoder().encodeToString(entity.getConteudo()));
        }

        return dto;
    }
}
