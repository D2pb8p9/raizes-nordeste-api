package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.request.ProcessarPagamentoRequest;
import com.raizesnordeste.api.api.dto.response.PagamentoResponse;
import com.raizesnordeste.api.application.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PostMapping
    public ResponseEntity<PagamentoResponse> processarPagamento(
            @Valid @RequestBody ProcessarPagamentoRequest request) {
        return ResponseEntity.ok(pagamentoService.processarPagamento(request));
    }
}

