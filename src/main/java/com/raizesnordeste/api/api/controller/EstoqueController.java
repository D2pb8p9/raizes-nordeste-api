package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.request.EntradaEstoqueRequest;
import com.raizesnordeste.api.api.dto.response.EstoqueResponse;
import com.raizesnordeste.api.application.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Estoque", description = "Controle de estoque por unidade")
@SecurityRequirement(name = "bearer-auth")
@RequiredArgsConstructor
@RequestMapping("/estoque")
@RestController
public class EstoqueController {

    private final EstoqueService estoqueService;

    @Operation(summary = "Registrar entrada de estoque",
            description = "Adiciona unidades de um produto ao estoque. Acesso: GERENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Entrada registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Unidade ou produto não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping("/entrada")
    public ResponseEntity<EstoqueResponse> registrarEntrada(
            @RequestBody @Valid EntradaEstoqueRequest request) {
        return ResponseEntity.status(201).body(estoqueService.registrarEntrada(request));
    }

    @Operation(summary = "Consultar estoque por unidade",
            description = "Retorna o saldo atual de todos os produtos de uma unidade. Acesso: GERENTE, FRANQUEADORA")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Estoque retornado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{unidadeId}")
    public ResponseEntity<List<EstoqueResponse>> consultarPorUnidade(
            @PathVariable Long unidadeId) {
        return ResponseEntity.ok(estoqueService.consultarPorUnidade(unidadeId));
    }
}
