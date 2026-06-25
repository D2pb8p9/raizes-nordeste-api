package com.raizesnordeste.api.api.dto.request;

import jakarta.validation.constraints.NotBlank;

public record ProdutoRequest(
        @NotBlank
        String nome,

        @NotBlank
        String descricao
) {
}
