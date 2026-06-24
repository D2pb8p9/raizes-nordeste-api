package com.raizesnordeste.api.domain;

import com.raizesnordeste.api.domain.enums.CanalPedido;
import com.raizesnordeste.api.domain.enums.StatusPagamento;
import com.raizesnordeste.api.domain.enums.StatusPedido;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Unidade unidade;

    @ManyToOne
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    private CanalPedido canalPedido;

    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @Enumerated(EnumType.STRING)
    private StatusPagamento statusPagamento;

    private LocalDateTime dataPedido;

    @OneToMany(mappedBy = "pedido")
    private List<ItensPedido> itens;

    private BigDecimal valorTotal;
}
