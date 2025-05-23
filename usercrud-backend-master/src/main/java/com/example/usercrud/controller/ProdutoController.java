package com.example.usercrud.controller;

import com.example.usercrud.dtos.ProdutoRequisicaoDTO;
import com.example.usercrud.dtos.ProdutoRespostaDTO;
import com.example.usercrud.model.Produto;
import com.example.usercrud.repository.ProdutoRepository;
import com.example.usercrud.service.ProdutoService;
import jakarta.validation.Valid;
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

    // Listar todos os produtos paginados
    @Transactional(readOnly = true)
    @GetMapping("/listarTodos")
    public ResponseEntity<Page<ProdutoRespostaDTO>> listarProdutos(
            @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Produto> page = repository.findAll(pageable);
        Page<ProdutoRespostaDTO> dtoPage = page.map(ProdutoRespostaDTO::getResponseComImagens);
        return ResponseEntity.ok(dtoPage);
    }

    // Cadastrar um novo produto
    @PostMapping("/cadastrar")
    public ResponseEntity<Object> cadastrarProduto(@Valid @RequestBody ProdutoRequisicaoDTO requestDTO) {
        try {
            var produto = service.cadastrarProduto(requestDTO);
            return ResponseEntity.ok(produto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    // Buscar um produto por ID
    @Transactional(readOnly = true)
    @GetMapping("/buscarProduto/{id}")
    public ResponseEntity<Object> buscarProdutoPorId(@PathVariable Long id) {
        Optional<Produto> produtoOpt = repository.findById(id);
        if (produtoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Produto não encontrado.");
        }

        ProdutoRespostaDTO responseDTO = ProdutoRespostaDTO.getResponseComImagens(produtoOpt.get());
        return ResponseEntity.ok(responseDTO);
    }

    // Alterar status do produto
    @Transactional
    @PutMapping("/{id}/alterarStatus")
    public ResponseEntity<Object> alterarStatus(@PathVariable Long id) {
        Optional<Produto> produtoOpt = repository.findById(id);
        if (produtoOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Produto não encontrado.");
        }

        Produto produto = produtoOpt.get();
        produto.alterarStatus();
        repository.save(produto);

        return ResponseEntity.ok("Status alterado com sucesso.");
    }

    @Transactional
    @PutMapping("/{id}/alterar")
    public ResponseEntity<Object> atualizarProduto(@PathVariable Long id, @Valid @RequestBody ProdutoRequisicaoDTO requestDTO) {
        try {
            service.editar(id, requestDTO.getUsuarioId(), requestDTO); // <-- passa o ID aqui
            return ResponseEntity.ok("Produto atualizado com sucesso.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao atualizar produto: " + e.getMessage());
        }
    }
}