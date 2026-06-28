package com.raizesnordeste.api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record CardapioItemRequest(
        @Schema(description = "ID do produto", example = "1")
        Long produtoId,

        @Schema(description = "ID da unidade", example = "1")
        Long unidadeId,

        @Schema(description = "Preço do produto nesta unidade", example = "29.90")
        BigDecimal preco,

        @Schema(description = "Se o item está disponível no cardápio", example = "true")
        Boolean disponivel
) {
}
