package com.raizesnordeste.api.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(description = "E-mail cadastrado", example = "joao@email.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "Senha do usuário", example = "senha123")
        @NotBlank
        String senha
) {
}
