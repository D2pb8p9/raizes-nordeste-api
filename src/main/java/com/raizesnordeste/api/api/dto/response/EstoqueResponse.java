package com.raizesnordeste.api.api.dto.response;

public record EstoqueResponse(
        Long produtoId,
        String nomeProduto,
        Integer quantidadeAtual
) {
}
