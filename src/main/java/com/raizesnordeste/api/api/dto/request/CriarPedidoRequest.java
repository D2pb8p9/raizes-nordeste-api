package com.raizesnordeste.api.api.dto.request;

import com.raizesnordeste.api.domain.enums.CanalPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CriarPedidoRequest(
        @Schema(description = "ID da unidade onde o pedido será feito", example = "1")
        @NotNull(message = "unidadeId é obrigatório")
        Long unidadeId,

        @Schema(
                description = "Canal pelo qual o pedido foi feito",
                example = "APP",
                allowableValues = {"APP", "TOTEM", "BALCAO", "PICKUP"}
        )
        @NotNull(message = "canalPedido é obrigatório")
        CanalPedido canalPedido,

        @Schema(description = "Lista de itens do pedido")
        @NotEmpty(message = "o pedido deve ter pelo menos um item")
        @Valid
        List<ItemPedidoRequest> itens
) {
}
