package com.example.usercrud.service;

import com.example.usercrud.dtos.ImagensDTO;
import com.example.usercrud.model.ImagemProduto;
import com.example.usercrud.model.Produto;
import com.example.usercrud.repository.ImagensRepository;
import com.example.usercrud.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Objects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ImagemService {

    private final ProdutoRepository produtoRepository;
    private final ImagensRepository imagensRepository;

    public ImagemService(ProdutoRepository produtoRepository, ImagensRepository imagensRepository) {
        this.produtoRepository = produtoRepository;
        this.imagensRepository = imagensRepository;
    }

    public List<ImagemProduto> uploadImagens(MultipartFile[] imagens, String[] urls, Boolean[] principais, Long produtoId) {
        try {
            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            List<ImagemProduto> imagensSalvas = new ArrayList<>();
            boolean temImagemPrincipal = produto.getImagens().stream()
                    .anyMatch(ImagemProduto::isImagemPrincipal);
            boolean algumaNovaPrincipal = principais != null && java.util.Arrays.stream(principais).anyMatch(Boolean::booleanValue);

            for (int i = 0; i < imagens.length; i++) {
                MultipartFile file = imagens[i];
                if (file.isEmpty()) continue;

                ImagemProduto imagem = new ImagemProduto();
                String url = (urls != null && i < urls.length) ? urls[i] : file.getOriginalFilename();
                imagem.setURL(url);
                boolean isPrincipal = (principais != null && i < principais.length) && Boolean.TRUE.equals(principais[i]);
                imagem.setImagemPrincipal(isPrincipal);
                imagem.setConteudo(file.getBytes());
                imagem.setProduto(produto);

                imagensSalvas.add(imagensRepository.save(imagem));
            }

            if (!temImagemPrincipal && !algumaNovaPrincipal && !imagensSalvas.isEmpty()) {
                ImagemProduto primeiraImagem = imagensSalvas.get(0);
                primeiraImagem.setImagemPrincipal(true);
                imagensRepository.save(primeiraImagem);
            }

            return imagensSalvas;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar as imagens: " + e.getMessage(), e);
        }
    }

    public void atualizarImagensDoProduto(MultipartFile[] files, String[] urls, Boolean[] principais, Long[] ids, Long produtoId) {
        try {
            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            List<ImagemProduto> imagensAtualizadas = new ArrayList<>();
            List<Long> idsRecebidos = (ids != null) ? List.of(ids) : List.of();

            if (ids != null && files != null) {
                for (int i = 0; i < ids.length; i++) {
                    Long imageId = ids[i];

                    if (imageId == 0 && i < files.length && !files[i].isEmpty()) {
                        ImagemProduto novaImagem = criarNovaImagem(produto, files[i],
                                (urls != null && i < urls.length) ? urls[i] : null,
                                (principais != null && i < principais.length) ? principais[i] : false);
                        imagensAtualizadas.add(novaImagem);

                    } else if (imageId > 0) {
                        ImagemProduto imagem = imagensRepository.findById(imageId)
                                .orElseThrow(() -> new RuntimeException("Imagem com ID " + imageId + " não encontrada"));

                        if (imagem.getProduto() == null || !Objects.equals(imagem.getProduto().getId(), produtoId)) {
                            throw new RuntimeException("A imagem não pertence a este produto");
                        }
                        atualizarImagemExistente(imagem,
                                (i < files.length) ? files[i] : null,
                                (urls != null && i < urls.length) ? urls[i] : null,
                                (principais != null && i < principais.length) ? principais[i] : null);

                        imagensAtualizadas.add(imagem);
                    }
                }
            }

            List<ImagemProduto> imagensParaRemover = new ArrayList<>();
            for (ImagemProduto img : produto.getImagens()) {
                if (!idsRecebidos.contains(img.getId()) && !imagensAtualizadas.contains(img)) {
                    imagensParaRemover.add(img);
                }
            }

            for (ImagemProduto img : imagensParaRemover) {
                produto.getImagens().remove(img);
                imagensRepository.delete(img);
            }

            boolean temPrincipal = imagensAtualizadas.stream().anyMatch(ImagemProduto::isImagemPrincipal);
            if (!temPrincipal && !imagensAtualizadas.isEmpty()) {
                ImagemProduto primeiraImagem = imagensAtualizadas.get(0);
                primeiraImagem.setImagemPrincipal(true);
            }

            imagensRepository.saveAll(imagensAtualizadas);
            produtoRepository.save(produto);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao atualizar imagens: " + e.getMessage(), e);
        }
    }

    private ImagemProduto criarNovaImagem(Produto produto, MultipartFile file, String url, Boolean principal) throws IOException {
        ImagemProduto imagem = new ImagemProduto();
        imagem.setURL(url != null ? url : file.getOriginalFilename());
        imagem.setImagemPrincipal(principal != null && principal);
        imagem.setConteudo(file.getBytes());
        imagem.setProduto(produto);
        return imagensRepository.save(imagem);
    }

    private void atualizarImagemExistente(ImagemProduto imagem, MultipartFile file, String url, Boolean principal) throws IOException {
        if (url != null) imagem.setURL(url);
        if (principal != null) imagem.setImagemPrincipal(principal);
        if (file != null && !file.isEmpty()) imagem.setConteudo(file.getBytes());
        imagensRepository.save(imagem);
    }
}
