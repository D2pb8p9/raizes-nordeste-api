package com.raizesnordeste.api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record EntradaEstoqueRequest(
        @Schema(description = "ID da unidade", example = "1")
        @NotNull(message = "unidadeId é obrigatório")
        Long unidadeId,

        @Schema(description = "ID do produto", example = "1")
        @NotNull(message = "produtoId é obrigatório")
        Long produtoId,

        @Schema(description = "Quantidade a adicionar", example = "50")
        @NotNull(message = "quantidade é obrigatória")
        @Min(value = 1, message = "quantidade deve ser pelo menos 1")
        Integer quantidade
) {
}
