package com.raizesnordeste.api.api.dto.request;

import com.raizesnordeste.api.domain.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record AtualizarStatusPedidoRequest(

        @NotNull(message = "status é obrigatório")
        StatusPedido status

) {
}
