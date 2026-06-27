package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.request.UnidadeRequest;
import com.raizesnordeste.api.api.dto.response.UnidadeResponse;
import com.raizesnordeste.api.application.service.UnidadeService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Unidades", description = "Gerenciamento das unidades da rede")
@SecurityRequirement(name = "bearer-auth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    private final UnidadeService unidadeService;

    @Operation(summary = "Listar unidades",
            description = "Retorna todas as unidades cadastradas. Acesso: GERENTE, FRANQUEADORA")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    public ResponseEntity<List<UnidadeResponse>> listar() {
        return ResponseEntity.ok(unidadeService.listarUnidades());
    }

    @Operation(summary = "Buscar unidade por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Unidade encontrada"),
            @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UnidadeResponse> getUnidade(@PathVariable Long id) {
        return ResponseEntity.ok(unidadeService.buscarUnidadePorId(id));
    }

    @Operation(summary = "Cadastrar unidade")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Unidade cadastrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<UnidadeResponse> cadastrar(
            @RequestBody @Valid UnidadeRequest unidadeRequest) {
        return ResponseEntity.status(201).body(unidadeService.cadastrarUnidade(unidadeRequest));
    }

    @Operation(summary = "Atualizar unidade")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Unidade atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Unidade não encontrada"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UnidadeResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid UnidadeRequest unidadeRequest) {
        return ResponseEntity.ok(unidadeService.atualizarUnidade(id, unidadeRequest));
    }
}
