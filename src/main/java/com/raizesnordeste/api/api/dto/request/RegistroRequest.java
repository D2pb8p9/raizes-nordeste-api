package com.raizesnordeste.api.api.dto.request;

import com.raizesnordeste.api.domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroRequest(
        @Schema(description = "Nome completo do usuário", example = "João Silva")
        @NotBlank
        String nome,

        @Schema(description = "E-mail usado no login", example = "joao@email.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "Senha do usuário", example = "senha123")
        @NotBlank
        String senha,

        @Schema(
                description = "Perfil de acesso",
                example = "CLIENTE",
                allowableValues = {"CLIENTE", "ATENDENTE", "COZINHEIRO", "GERENTE", "FRANQUEADORA"}
        )
        @NotNull
        Role role
) {
}
