package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.request.ProdutoRequest;
import com.raizesnordeste.api.api.dto.response.ProdutoResponse;
import com.raizesnordeste.api.application.service.ProdutoService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> listar(){
        List<ProdutoResponse> produtos = produtoService.listarProdutos();
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> getProduto(@PathVariable Long id){
        ProdutoResponse produtoEncontrado = produtoService.buscarProdutoPorId(id);
        return ResponseEntity.ok(produtoEncontrado);
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> cadastrar(@RequestBody @Valid ProdutoRequest produtoRequest){
        ProdutoResponse produtoSalvo = produtoService.cadastrarProduto(produtoRequest);
        return ResponseEntity.status(201).body(produtoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id,
                                                     @RequestBody @Valid ProdutoRequest produtoRequest) {
        ProdutoResponse produtoAtualizado = produtoService.atualizarProduto(id, produtoRequest);
        return ResponseEntity.ok(produtoAtualizado);
    }
}
