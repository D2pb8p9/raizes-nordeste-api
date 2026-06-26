package com.raizesnordeste.api.api.dto.response;

import java.math.BigDecimal;

public record CardapioItemResponse(
        Long id,
        Long produtoId,
        String nomeProduto,
        Long unidadeId,
        String nomeUnidade,
        BigDecimal preco,
        Boolean disponivel
) {
}
