package com.example.usercrud.service;

import com.example.usercrud.model.Produto;
import com.example.usercrud.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;

    public Produto criarProduto(Produto produto) {
        return produtoRepository.save(produto);
    }

    public List<Produto> listarProdutos() {
        return produtoRepository.findAll();
    }

    public Produto alterarProduto(Long id, Produto attProduto) {
        Optional<Produto> existeProduto = produtoRepository.findById(id);
        if (existeProduto.isPresent()) {
            Produto produto = existeProduto.get();
            produto.setNome(attProduto.getNome());
            produto.setCodigo(attProduto.getCodigo());
            produto.setQuantidade(attProduto.getQuantidade());
            produto.setPreco(attProduto.getPreco());
        }
        return null;
    }

    public Produto buscarProduto(Long id) {
        return produtoRepository.findById(id).orElse(null);
    }

    public boolean excluirProduto(Long id) {
        Optional<Produto> produtoExiste = produtoRepository.findById(id);
        if (produtoExiste.isPresent()) {
            produtoRepository.delete(produtoExiste.get());
            return true;
        }
        return false;
    }
}
