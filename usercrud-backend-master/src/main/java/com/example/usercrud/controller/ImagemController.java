package com.example.usercrud.controller;

import com.example.usercrud.model.ImagemProduto;
import com.example.usercrud.service.ImagemService;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/imagens")
@CrossOrigin("*")
public class ImagemController {

    private final ImagemService imagemService;

    public ImagemController(ImagemService imagemService) {
        this.imagemService = imagemService;
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<List<ImagemProduto>> adicionarMultiplasImagens(
            @PathVariable Long id,
            @RequestParam(value = "imagens", required = false) MultipartFile[] files,
            @RequestParam(value = "urls", required = false) String[] urls,
            @RequestParam(value = "principais", required = false) Boolean[] principais) {

        try {
            return ResponseEntity.ok().body(imagemService.uploadImagens(files, urls, principais, id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}/update_imagens")
    @Transactional(rollbackOn = Exception.class)
    public ResponseEntity<?> atualizarImagensDoProduto(
            @PathVariable Long id,
            @RequestParam(value = "ids", required = false) Long[] ids,
            @RequestParam(value = "imagens", required = false) MultipartFile[] files,
            @RequestParam(value = "urls", required = false) String[] urls,
            @RequestParam(value = "principais", required = false) Boolean[] principais) {

        try {
            imagemService.atualizarImagensDoProduto(files, urls, principais, ids, id);
            return ResponseEntity.ok().body("Imagens atualizadas com sucesso");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar imagens: " + e.getMessage());
        }
    }
}
