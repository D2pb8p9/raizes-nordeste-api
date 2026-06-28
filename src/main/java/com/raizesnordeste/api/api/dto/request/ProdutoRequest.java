package com.raizesnordeste.api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ProdutoRequest(
        @Schema(description = "Nome do produto", example = "Baião de Dois")
        @NotBlank
        String nome,

        @Schema(description = "Descrição do produto",
                example = "Arroz com feijão, queijo coalho e carne seca")
        @NotBlank
        String descricao
) {
}
