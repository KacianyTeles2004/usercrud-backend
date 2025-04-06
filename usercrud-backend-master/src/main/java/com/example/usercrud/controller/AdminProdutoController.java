package com.example.usercrud.controller;


import com.example.usercrud.service.ProdutoService;
import com.example.usercrud.model.Produto;
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

        return produtoService.buscarProduto(id);
    }
}