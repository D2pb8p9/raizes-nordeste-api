package com.raizesnordeste.api.infrastructure.repository;

import com.raizesnordeste.api.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
