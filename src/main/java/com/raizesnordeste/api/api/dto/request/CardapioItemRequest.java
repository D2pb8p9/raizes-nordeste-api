package com.raizesnordeste.api.api.dto.request;

import java.math.BigDecimal;

public record CardapioItemRequest(
        Long produtoId,
        Long unidadeId,
        BigDecimal preco,
        Boolean disponivel
) {
}
