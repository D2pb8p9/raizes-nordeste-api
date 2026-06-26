package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.request.CardapioItemRequest;
import com.raizesnordeste.api.api.dto.response.CardapioItemResponse;
import com.raizesnordeste.api.application.service.CardapioItemService;
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

@RequiredArgsConstructor
@RequestMapping("/cardapio")
@RestController
public class CardapioItemController {

    private final CardapioItemService cardapioItemService;

    @GetMapping
    public ResponseEntity<List<CardapioItemResponse>> listarCardapioItens(@RequestParam Long unidadeId) {
        List<CardapioItemResponse> listaItens = cardapioItemService.listarItensPorUnidade(unidadeId);
        return ResponseEntity.ok(listaItens);
    }

    @PostMapping
    public ResponseEntity<CardapioItemResponse> cadastrarItem(
            @RequestBody @Valid CardapioItemRequest cardapioItemRequest) {
        CardapioItemResponse itemCadastrado = cardapioItemService.cadastrarItem(cardapioItemRequest);
        return ResponseEntity.status(201).body(itemCadastrado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardapioItemResponse>  atualizarItem(
            @RequestBody @Valid CardapioItemRequest cardapioItemRequest,
            @PathVariable Long id) {
        CardapioItemResponse itemAtualizado = cardapioItemService.atualizarItem(cardapioItemRequest, id);
        return ResponseEntity.status(200).body(itemAtualizado);
    }
}
