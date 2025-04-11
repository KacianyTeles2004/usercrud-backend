package com.example.usercrud.service;


import com.example.usercrud.dtos.ProdutoRequisicaoDTO;
import com.example.usercrud.model.ImagemProduto;
import com.example.usercrud.model.Produto;
import com.example.usercrud.model.TiposUsuarios;
import com.example.usercrud.model.Usuario;
import com.example.usercrud.repository.ImagensRepository;
import com.example.usercrud.repository.ProdutoRepository;
import com.example.usercrud.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    private final ProdutoRepository repository;
    private final ImagensRepository imagensRepository;
    private final UsuarioRepository usuarioRepository;

    public ProdutoService(ProdutoRepository repository, ImagensRepository imagensRepository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.imagensRepository = imagensRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Produto cadastrarProduto(ProdutoRequisicaoDTO requestDTO) {
        try {
            // Validações
            if (requestDTO.getNomeProduto().length() > 200) {
                throw new RuntimeException("O nome do produto ultrapassa o limite");
            }

            if (requestDTO.getAvaliacao() > 5 || requestDTO.getAvaliacao() < 0) {
                throw new RuntimeException("A nota do produto não pode ser maior que 5");
            }

            if (requestDTO.getAvaliacao() % 0.5 != 0) {
                throw new RuntimeException("A avaliação deve ser um número múltiplo de 0.5 (ex: 0.5, 1, 1.5, ..., 5)");
            }

            if (requestDTO.getDescricao().length() > 2000) {
                throw new RuntimeException("A descrição do produto ultrapassa o limite");
            }

            // Criar e configurar o produto
            Produto produto = new Produto();
            produto.setNomeProduto(requestDTO.getNomeProduto());
            produto.setAvaliacao(requestDTO.getAvaliacao());
            produto.setDescricao(requestDTO.getDescricao());
            produto.setPreco(requestDTO.getPreco());
            produto.setQuantEstoque(requestDTO.getQuantEstoque());
            produto.setAtivo(true);

            // Salvar o produto para obter o ID
            Produto produtoSalvo = repository.save(produto);

            // Processar as imagens se houver
            if (requestDTO.getImagens() != null && !requestDTO.getImagens().isEmpty()) {
                List<ImagemProduto> imagens = new ArrayList<>();

                for (ImagemProduto imagemDTO : requestDTO.getImagens()) {
                    ImagemProduto imagem = new ImagemProduto();
                    imagem.setURL(imagemDTO.getURL());
                    imagem.setImagemPrincipal(imagemDTO.isImagemPrincipal());
                    imagem.setConteudo(imagemDTO.getConteudo());
                    imagem.setProduto(produtoSalvo);
                    imagens.add(imagem);
                }

                imagensRepository.saveAll(imagens);
                produtoSalvo.setImagens(imagens);
            }

            return produtoSalvo;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar o produto: " + e.getMessage());
        }
    }

    @Transactional
    public Produto editar(Long id, ProdutoRequisicaoDTO requestDTO) {
        try {

            Usuario usuario = usuarioRepository.findById(id).get();

            //Verifica se o produto existe no banco
            Optional<Produto> produtoOpt = repository.findById(requestDTO.getId());
            if (produtoOpt.isEmpty()) {
                throw new RuntimeException("Produto não encontrado");
            }

            //pega produto do banco
            Produto produto = produtoOpt.get();

            // verifica se o usuário é estoquista
            if (usuario.getTipo() == TiposUsuarios.STOCKIST) {
                // Estoquistas podem atualizar apenas a quantidade do estoque
                produto.setQuantEstoque(requestDTO.getQuantEstoque());
            } else {
                // Outros usuários (ex: ADMIN) podem atualizar tudo
                if (requestDTO.getNomeProduto().length() > 200) {
                    throw new RuntimeException("O nome do produto ultrapassa o limite");
                }

                if (requestDTO.getAvaliacao() > 5 || requestDTO.getAvaliacao() < 0) {
                    throw new RuntimeException("A nota do produto não pode ser maior que 5");
                }

                if (requestDTO.getDescricao().length() > 2000) {
                    throw new RuntimeException("A descrição do produto ultrapassa o limite");
                }

                // Atualizar os dados do produto
                produto.setNomeProduto(requestDTO.getNomeProduto());
                produto.setDescricao(requestDTO.getDescricao());
                produto.setPreco(requestDTO.getPreco());
                produto.setQuantEstoque(requestDTO.getQuantEstoque());
                produto.setAtivo(requestDTO.isAtivo());
                produto.setAvaliacao(requestDTO.getAvaliacao());

                // Salva e retorna o produto atualizado
            }
            return repository.save(produto);
        } catch (RuntimeException e) {
            throw new RuntimeException("Erro ao atualizar o produto: " + e.getMessage());
        }
    }

}
