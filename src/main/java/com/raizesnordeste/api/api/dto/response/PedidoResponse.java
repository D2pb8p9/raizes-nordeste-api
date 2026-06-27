package com.raizesnordeste.api.api.dto.response;

import com.raizesnordeste.api.domain.enums.CanalPedido;
import com.raizesnordeste.api.domain.enums.StatusPagamento;
import com.raizesnordeste.api.domain.enums.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        Long unidadeId,
        String nomeUnidade,
        Long usuarioId,
        String nomeUsuario,
        CanalPedido canalPedido,
        StatusPedido status,
        StatusPagamento statusPagamento,
        LocalDateTime dataPedido,
        BigDecimal valorTotal,
        List<ItemPedidoResponse> itens
) {
}
