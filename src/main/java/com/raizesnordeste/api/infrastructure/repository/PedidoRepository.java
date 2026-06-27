package com.raizesnordeste.api.infrastructure.repository;

import com.raizesnordeste.api.domain.Pedido;
import com.raizesnordeste.api.domain.enums.CanalPedido;
import com.raizesnordeste.api.domain.enums.StatusPedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query("SELECT p FROM Pedido p WHERE p.unidade.id = :unidadeId " +
            "AND (:status IS NULL OR p.status = :status) " +
            "AND (:canalPedido IS NULL OR p.canalPedido = :canalPedido)")
    List<Pedido> findByFiltros(
            @Param("unidadeId") Long unidadeId,
            @Param("status") StatusPedido status,
            @Param("canalPedido") CanalPedido canalPedido);
}
