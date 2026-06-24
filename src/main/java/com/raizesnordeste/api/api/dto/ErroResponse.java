package com.raizesnordeste.api.api.dto;

public record ErroResponse(
        String error,
        String message,
        String timestamp,
        String path
) {
}
