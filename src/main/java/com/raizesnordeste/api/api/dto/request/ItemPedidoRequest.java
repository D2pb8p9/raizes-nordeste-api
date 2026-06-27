package com.raizesnordeste.api.api.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemPedidoRequest(

        @NotNull(message = "produtoId é obrigatório")
        Long produtoId,

        @NotNull(message = "quantidade é obrigatória")
        @Min(value = 1, message = "quantidade deve ser pelo menos 1")
        Integer quantidade,

        String observacao

) {
}
