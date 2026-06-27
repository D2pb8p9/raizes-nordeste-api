package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.request.AtualizarStatusPedidoRequest;
import com.raizesnordeste.api.api.dto.request.CriarPedidoRequest;
import com.raizesnordeste.api.api.dto.response.PedidoResponse;
import com.raizesnordeste.api.application.service.PedidoService;
import com.raizesnordeste.api.domain.enums.CanalPedido;
import com.raizesnordeste.api.domain.enums.StatusPedido;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/pedidos")
@RestController
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(
            @RequestBody @Valid CriarPedidoRequest request,
            Authentication authentication) {
        PedidoResponse response = pedidoService.criarPedido(request, authentication.getName());
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("/{unidadeId}")
    public ResponseEntity<List<PedidoResponse>> consultarPedidos(
            @PathVariable Long unidadeId,
            @RequestParam(required = false) StatusPedido status,
            @RequestParam(required = false) CanalPedido canalPedido) {
        List<PedidoResponse> pedidos = pedidoService.consultarPedidos(unidadeId, status, canalPedido);
        return ResponseEntity.ok(pedidos);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarStatusPedidoRequest request) {
        PedidoResponse response = pedidoService.atualizarStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelarPedido(@PathVariable Long id) {
        PedidoResponse response = pedidoService.cancelarPedido(id);
        return ResponseEntity.ok(response);
    }
}

