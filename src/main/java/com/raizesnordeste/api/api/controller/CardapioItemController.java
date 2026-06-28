package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.request.CardapioItemRequest;
import com.raizesnordeste.api.api.dto.response.CardapioItemResponse;
import com.raizesnordeste.api.application.service.CardapioItemService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Cardápio", description = "Gerenciamento dos itens do cardápio por unidade")
@SecurityRequirement(name = "bearer-auth")
@RequiredArgsConstructor
@RequestMapping("/cardapio")
@RestController
public class CardapioItemController {

    private final CardapioItemService cardapioItemService;

    @Operation(summary = "Listar cardápio de uma unidade",
            description = "Retorna os itens do cardápio filtrados por unidade. Acesso: autenticado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping
    public ResponseEntity<List<CardapioItemResponse>> listarCardapioItens(
            @RequestParam Long unidadeId) {
        return ResponseEntity.ok(cardapioItemService.listarItensPorUnidade(unidadeId));
    }

    @Operation(summary = "Cadastrar item no cardápio")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409",
                    description = "Item já cadastrado para este produto nesta unidade"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<CardapioItemResponse> cadastrarItem(
            @RequestBody @Valid CardapioItemRequest cardapioItemRequest) {
        return ResponseEntity.status(201).body(
                cardapioItemService.cadastrarItem(cardapioItemRequest));
    }

    @Operation(summary = "Atualizar item do cardápio")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Item atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Item não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CardapioItemResponse> atualizarItem(
            @RequestBody @Valid CardapioItemRequest cardapioItemRequest,
            @PathVariable Long id) {
        return ResponseEntity.ok(cardapioItemService.atualizarItem(cardapioItemRequest, id));
    }
}
