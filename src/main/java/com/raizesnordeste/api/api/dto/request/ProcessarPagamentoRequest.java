package com.raizesnordeste.api.api.dto.request;

import com.raizesnordeste.api.domain.enums.TipoPagamento;
import jakarta.validation.constraints.NotNull;

public record ProcessarPagamentoRequest(
        @NotNull
        Long pedidoId,

        @NotNull
        TipoPagamento tipoPagamento
) {
}
