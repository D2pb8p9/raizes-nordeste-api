package com.raizesnordeste.api.api.dto.response;

public record ErroResponse(
        String error,
        String message,
        String timestamp,
        String path
) {
}
