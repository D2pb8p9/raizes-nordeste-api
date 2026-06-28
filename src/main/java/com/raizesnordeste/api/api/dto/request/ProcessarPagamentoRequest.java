package com.raizesnordeste.api.api.dto.request;

import com.raizesnordeste.api.domain.enums.TipoPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ProcessarPagamentoRequest(
        @Schema(description = "ID do pedido a ser pago", example = "1")
        @NotNull
        Long pedidoId,

        @Schema(
                description = "Forma de pagamento",
                example = "PIX",
                allowableValues = {"PIX", "CARTAO_CREDITO", "CARTAO_DEBITO", "DINHEIRO"}
        )
        @NotNull
        TipoPagamento tipoPagamento
) {
}
