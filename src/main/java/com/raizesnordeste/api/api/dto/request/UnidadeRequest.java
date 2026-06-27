package com.raizesnordeste.api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UnidadeRequest(
        @Schema(description = "Nome da unidade", example = "Unidade Fortaleza")
        @NotBlank
        String nome,

        @Schema(description = "Endereço físico da unidade",
                example = "Av. Beira Mar, 1000 - Fortaleza/CE")
        @NotBlank
        String endereco
) {
}
