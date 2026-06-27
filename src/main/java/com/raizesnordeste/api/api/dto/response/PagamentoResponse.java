package com.raizesnordeste.api.api.dto.response;

import com.raizesnordeste.api.domain.enums.StatusPagamento;
import com.raizesnordeste.api.domain.enums.TipoPagamento;
import java.time.LocalDateTime;

public record PagamentoResponse(
        Long id,
        Long pedidoId,
        TipoPagamento tipoPagamento,
        StatusPagamento statusPagamento,
        LocalDateTime dataPagamento

) {
}
