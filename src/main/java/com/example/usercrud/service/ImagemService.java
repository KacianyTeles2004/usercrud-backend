package com.example.usercrud.service;

import com.example.usercrud.dtos.ImagensDTO;
import com.example.usercrud.model.ImagemProduto;
import com.example.usercrud.model.Produto;
import com.example.usercrud.repository.ImagensRepository;
import com.example.usercrud.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

            //vai guardar as imagens que vão pro banco
            List<ImagemProduto> imagensSalvas = new ArrayList<>();

            // Verifica se o produto já tem uma imagem principal
            boolean temImagemPrincipal = produto.getImagens().stream()
                    .anyMatch(ImagemProduto::isImagemPrincipal);

            // Verifica se alguma das novas imagens está marcada como principal
            boolean algumaNovaPrincipal = false;
            if (principais != null) {
                for (Boolean principal : principais) {
                    if (principal != null && principal) {
                        algumaNovaPrincipal = true;
                        break;
                    }
                }
            }
            // varre a lista de arquivos e verifica se não está vazio
            for (int i = 0; i < imagens.length; i++) {
                MultipartFile file = imagens[i];
                if (file.isEmpty()) continue;

                ImagemProduto imagem = new ImagemProduto();

                // Se vir URL, usa ela, senão usa o nome do arquivo
                String url = (urls != null && i < urls.length) ? urls[i] : file.getOriginalFilename();
                imagem.setURL(url);

                // Define se é imagem principal
                boolean isPrincipal = false;
                if (principais != null && i < principais.length) {
                    isPrincipal = principais[i];
                }
                imagem.setImagemPrincipal(isPrincipal);

                imagem.setConteudo(file.getBytes());
                imagem.setProduto(produto);

                imagensSalvas.add(imagensRepository.save(imagem));
            }

            // Se não há imagem principal (nem nas existentes nem nas novas),
            // marca a primeira imagem nova como principal
            if (!temImagemPrincipal && !algumaNovaPrincipal && !imagensSalvas.isEmpty()) {
                ImagemProduto primeiraImagem = imagensSalvas.get(0);
                primeiraImagem.setImagemPrincipal(true);
                imagensRepository.save(primeiraImagem);
            }

            return imagensSalvas;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar as imagens: " + e.getMessage());
        }

    }

    public void atualizarImagensDopruduto(MultipartFile[] files, String[] urls, Boolean[] principais, Long[] ids, Long produtoId) {
        try {
            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            List<ImagemProduto> imagensAtualizadas = new ArrayList<>();
            List<Long> idsRecebidos = ids != null ? List.of(ids) : List.of();

            // Verifica se o produto já tem imagens
            if (ids != null && files != null) {
                for (int i = 0; i < ids.length; i++) {
                    Long imageId = ids[i];

                    if (imageId == 0 && i < files.length && !files[i].isEmpty()) {
                        // Id 0 significa nova imagem
                        ImagemProduto novaImagem = criarNovaImagem(produto, files[i],
                                (urls != null && i < urls.length) ? urls[i] : null,
                                (principais != null && i < principais.length) ? principais[i] : false);

                        imagensAtualizadas.add(novaImagem);

                    } else if (imageId > 0) {
                        // Se o id é maior que zero é uma imagem existente
                        ImagemProduto imagem = imagensRepository.findById(imageId)
                                .orElseThrow(() -> new RuntimeException("Imagem com ID " + imageId + " não encontrada"));

                        if (imagem.getProduto().getId() != (produtoId)) {
                            throw new RuntimeException("A imagem não     pertence a este produto");
                        }
                        // Atualiza a imagem existente
                        atualizarImagemExistente(imagem,
                                (i < files.length) ? files[i] : null,
                                (urls != null && i < urls.length) ? urls[i] : null,
                                (principais != null && i < principais.length) ? principais[i] : null);

                        imagensAtualizadas.add(imagem)
                        ;
                    }
                }
            }

            // Remove as imagens que nao vieram na requisição, pois se não veio é pq foi removida
            if (!produto.getImagens().isEmpty()) {
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
            }
            boolean temPrincipal = imagensAtualizadas.stream()
                    .anyMatch(ImagemProduto::isImagemPrincipal);

// Se não houver imagem principal e existirem imagens, defina a primeira como principal
            if (!temPrincipal && !imagensAtualizadas.isEmpty()) {
                ImagemProduto primeiraimagem = imagensAtualizadas.get(0);
                primeiraimagem.setImagemPrincipal(true);
            }
            imagensRepository.saveAll(imagensAtualizadas);
            produtoRepository.save(produto);

            imagensAtualizadas.stream()
                    .map(ImagensDTO::fromEntity)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace(); // Add proper logging
            throw new RuntimeException("Erro ao atualizar imagens: " + e.getMessage());
        }


    }

    private ImagemProduto criarNovaImagem(Produto produto, MultipartFile file, String url, Boolean principal) throws IOException {
        ImagemProduto imagem = new ImagemProduto();
        imagem.setURL(url != null ? url : file.getOriginalFilename());
        imagem.setImagemPrincipal(principal);
        imagem.setConteudo(file.getBytes());
        imagem.setProduto(produto);
        return imagensRepository.save(imagem);
    }

    private void atualizarImagemExistente(ImagemProduto imagem, MultipartFile file, String url, Boolean principal) throws IOException {
        if (url != null) {
            imagem.setURL(url);
        }

        if (principal != null) {
            imagem.setImagemPrincipal(principal);
        }

        if (file != null && !file.isEmpty()) {
            imagem.setConteudo(file.getBytes());
        }

        imagensRepository.save(imagem);
    }
}

