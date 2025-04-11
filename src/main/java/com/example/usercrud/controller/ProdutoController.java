package com.example.usercrud.controller;

import com.example.usercrud.dtos.ProdutoRequisicaoDTO;
import com.example.usercrud.dtos.ProdutoRespostaDTO;
import com.example.usercrud.model.Produto;
import com.example.usercrud.repository.ProdutoRepository;
import com.example.usercrud.service.ProdutoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/produto")
@CrossOrigin("*")
public class ProdutoController {


    private final ProdutoRepository repository;
    private final ProdutoService service;


    public ProdutoController(ProdutoRepository produtoRepository, ProdutoService produtoService) {
        this.repository = produtoRepository;
        this.service = produtoService;
    }

    @Transactional(readOnly = true)
    @GetMapping("/listarTodos")
    public ResponseEntity<Page<ProdutoRespostaDTO>> lista(@PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Produto> page = repository.findAll(pageable);
        Page<ProdutoRespostaDTO> dtoPage = page.map(ProdutoRespostaDTO::getResponseComImagens);
        return ResponseEntity.ok(dtoPage);
    }


    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrarProduto(@RequestBody ProdutoRequisicaoDTO requestDTO) {
        try {
            var produto = service.cadastrarProduto(requestDTO);
            return ResponseEntity.ok().body(produto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    @GetMapping("/buscarProduto/{id}")
    public ResponseEntity<Object> buscarProdutoPorId(@PathVariable Long id) {
        try {
            Optional<Produto> produtoOpt = repository.findById(id);

            if (produtoOpt.isEmpty()) {
                return ResponseEntity.badRequest().body("Produto não encontrado");
            }

            ProdutoRespostaDTO responseDTO = ProdutoRespostaDTO.getResponseComImagens(produtoOpt.get());
            return ResponseEntity.ok(responseDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao buscar produto: " + e.getMessage());
        }
    }

    @Transactional
    @PutMapping("/{id}/alterarStatus")
    public ResponseEntity<Object> alterarStatus(@PathVariable Long id) { // Removendo @RequestBody
        try {
            Optional<Produto> produto = repository.findById(id);
            if (produto.isEmpty()) {
                return ResponseEntity.badRequest().body("Produto não encontrado");
            }

            Produto produtosave = produto.get();
            produtosave.alterarStatus();
            repository.save(produtosave);

            return ResponseEntity.ok().body("Status alterado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ops, ocorreu algum erro no processo");
        }
    }


    @PutMapping("/{id}/alterar")
    public ResponseEntity<Object> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoRequisicaoDTO requestDTO) {
        try {
            service.editar(id, requestDTO);
            return ResponseEntity.ok().body("Alterado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar o produto: " + e.getMessage());
        }
    }


}