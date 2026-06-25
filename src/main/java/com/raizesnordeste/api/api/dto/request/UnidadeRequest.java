package com.raizesnordeste.api.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UnidadeRequest(
        @NotBlank
        String nome,

        @NotBlank
        String endereco
) {
}
