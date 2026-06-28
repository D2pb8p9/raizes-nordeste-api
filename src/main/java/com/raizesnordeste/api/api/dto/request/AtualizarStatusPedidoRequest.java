package com.raizesnordeste.api.api.dto.request;

import com.raizesnordeste.api.domain.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusPedidoRequest(
        @Schema(
                description = "Novo status do pedido",
                example = "EM_PREPARO",
                allowableValues = {"RECEBIDO", "EM_PREPARO", "PRONTO", "ENTREGUE", "CANCELADO"}
        )
        @NotNull(message = "status é obrigatório")
        StatusPedido status
) {
}
