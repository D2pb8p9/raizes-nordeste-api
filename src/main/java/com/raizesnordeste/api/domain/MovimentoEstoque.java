package com.raizesnordeste.api.domain;

import com.raizesnordeste.api.domain.enums.TipoMovimentoEstoque;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class MovimentoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Unidade unidade;

    @ManyToOne
    private Produto produto;

    @ManyToOne
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    private TipoMovimentoEstoque tipoMovimento;

    private Integer quantidade;
    private LocalDateTime dataMovimento;
}
