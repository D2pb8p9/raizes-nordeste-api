package com.raizesnordeste.api.api.dto.response;

import java.math.BigDecimal;

public record ItemPedidoResponse(
        Long produtoId,
        String nomeProduto,
        Integer quantidade,
        BigDecimal precoUnitario,
        String observacao

) {
}
