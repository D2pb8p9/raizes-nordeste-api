package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.request.EntradaEstoqueRequest;
import com.raizesnordeste.api.api.dto.response.EstoqueResponse;
import com.raizesnordeste.api.application.service.EstoqueService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/estoque")
@RestController
public class EstoqueController {

    private final EstoqueService estoqueService;

    @PostMapping("/entrada")
    public ResponseEntity<EstoqueResponse> registrarEntrada(
            @RequestBody @Valid EntradaEstoqueRequest request) {
        EstoqueResponse response = estoqueService.registrarEntrada(request);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{unidadeId}")
    public ResponseEntity<List<EstoqueResponse>> consultarPorUnidade(
            @PathVariable Long unidadeId) {
        List<EstoqueResponse> estoques = estoqueService.consultarPorUnidade(unidadeId);
        return ResponseEntity.ok(estoques);
    }
}

