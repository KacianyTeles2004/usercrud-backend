package com.example.usCRUD.controller;

import com.example.usCRUD.model.Produto;
import com.example.usCRUD.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdminProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/admin/produto/{id}")
    public Produto visualizarProduto(@PathVariable Long id) {

        return produtoService.buscarProdutoPorId(id);
    }
}