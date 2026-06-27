package com.raizesnordeste.api.api.dto.response;

import java.time.LocalDateTime;

public record FidelidadeResponse(
        Long id,
        Long usuarioId,
        String nomeUsuario,
        Integer pontos,
        Boolean consentimentoAceito,
        LocalDateTime dataConsentimento
) {
}
