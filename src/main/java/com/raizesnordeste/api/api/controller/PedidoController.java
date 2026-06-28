package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.request.AtualizarStatusPedidoRequest;
import com.raizesnordeste.api.api.dto.request.CriarPedidoRequest;
import com.raizesnordeste.api.api.dto.response.PedidoResponse;
import com.raizesnordeste.api.application.service.PedidoService;
import com.raizesnordeste.api.domain.enums.CanalPedido;
import com.raizesnordeste.api.domain.enums.StatusPedido;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Pedidos", description = "Criação, consulta e atualização de status de pedidos")
@SecurityRequirement(name = "bearer-auth")
@RequiredArgsConstructor
@RequestMapping("/pedidos")
@RestController
public class PedidoController {

    private final PedidoService pedidoService;

    @Operation(summary = "Criar pedido",
            description = "Cria um novo pedido. O usuário é identificado pelo token JWT. Acesso: CLIENTE, ATENDENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "422", description = "Estoque insuficiente"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PostMapping
    public ResponseEntity<PedidoResponse> criarPedido(
            @RequestBody @Valid CriarPedidoRequest request,
            Authentication authentication) {
        return ResponseEntity.status(201).body(
                pedidoService.criarPedido(request, authentication.getName()));
    }

    @Operation(summary = "Consultar pedidos de uma unidade",
            description = "Lista os pedidos com filtros opcionais de status e canal. Acesso: ATENDENTE, COZINHEIRO, GERENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/{unidadeId}")
    public ResponseEntity<List<PedidoResponse>> consultarPedidos(
            @PathVariable Long unidadeId,
            @RequestParam(required = false) StatusPedido status,
            @RequestParam(required = false) CanalPedido canalPedido) {
        return ResponseEntity.ok(pedidoService.consultarPedidos(unidadeId, status, canalPedido));
    }

    @Operation(summary = "Consultar histórico do cliente",
            description = "Retorna todos os pedidos do cliente autenticado. Acesso: CLIENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @GetMapping("/meu-historico")
    public ResponseEntity<List<PedidoResponse>> consultarMeusPedidos(
            Authentication authentication) {
        return ResponseEntity.ok(pedidoService.consultarMeusPedidos(authentication.getName()));
    }

    @Operation(summary = "Atualizar status do pedido",
            description = "Avança o status: RECEBIDO → EM_PREPARO → PRONTO → ENTREGUE. Acesso: COZINHEIRO, ATENDENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Transição de status inválida"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponse> atualizarStatus(
            @PathVariable Long id,
            @RequestBody @Valid AtualizarStatusPedidoRequest request) {
        return ResponseEntity.ok(pedidoService.atualizarStatus(id, request));
    }

    @Operation(summary = "Cancelar pedido",
            description = "Cancela o pedido e estorna o estoque dos itens. Acesso: CLIENTE, ATENDENTE, GERENTE")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado"),
            @ApiResponse(responseCode = "403", description = "Acesso negado")
    })
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PedidoResponse> cancelarPedido(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.cancelarPedido(id));
    }
}
