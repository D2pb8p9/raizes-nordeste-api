package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.request.ProcessarPagamentoRequest;
import com.raizesnordeste.api.api.dto.response.PagamentoResponse;
import com.raizesnordeste.api.application.service.PagamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Pagamentos", description = "Processamento de pagamentos via gateway mock")
@SecurityRequirement(name = "bearer-auth")
@RequiredArgsConstructor
@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @Operation(summary = "Processar pagamento",
            description = "Envia ao gateway mock (80% aprovação) e atualiza o status do pedido. Acesso: CLIENTE, ATENDENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento processado"),
            @ApiResponse(responseCode = "400", description = "Pedido já foi pago"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<PagamentoResponse> processarPagamento(
            @Valid @RequestBody ProcessarPagamentoRequest request) {
        return ResponseEntity.ok(pagamentoService.processarPagamento(request));
    }
}
