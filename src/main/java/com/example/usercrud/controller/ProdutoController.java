package com.example.usercrud.controller;

import com.example.usercrud.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.usercrud.model.Produto;
import com.example.usercrud.repository.ProdutoRepository;

import javax.swing.plaf.ProgressBarUI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping("/listar")
    public ResponseEntity< List<Produto>> listarProduto(){
        List<Produto> listaProduto = produtoService.listarProdutos();
        return ResponseEntity.ok(listaProduto);
    }

    @PostMapping
    public ResponseEntity <Produto> resgitrarProduto(@RequestBody Produto produto){
        Produto produtoCriado = produtoService.criarProduto(produto);
        return new ResponseEntity<>(produto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity <Produto> alterarProduto(@PathVariable Long id, @RequestBody Produto produto){
        Produto produtoAtt = produtoService.alterarProduto(id, produto);
        return  ResponseEntity.ok(produtoAtt);
    }

    @DeleteMapping
    public  ResponseEntity <Void> excluirProduto(@PathVariable Long id){
        produtoService.excluirProduto(id);
        return ResponseEntity.noContent().build();
    }

}

