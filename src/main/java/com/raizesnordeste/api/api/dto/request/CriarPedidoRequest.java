package com.raizesnordeste.api.api.dto.request;

import com.raizesnordeste.api.domain.enums.CanalPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CriarPedidoRequest(

        @NotNull(message = "unidadeId é obrigatório")
        Long unidadeId,

        @NotNull(message = "usuarioId é obrigatório")
        Long usuarioId,

        @NotNull(message = "canalPedido é obrigatório")
        CanalPedido canalPedido,

        @NotEmpty(message = "o pedido deve ter pelo menos um item")
        @Valid
        List<ItemPedidoRequest> itens
) {
}
