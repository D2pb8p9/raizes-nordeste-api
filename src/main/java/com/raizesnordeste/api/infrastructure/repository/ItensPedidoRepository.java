package com.raizesnordeste.api.infrastructure.repository;

import com.raizesnordeste.api.domain.ItensPedido;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItensPedidoRepository extends JpaRepository<ItensPedido, Long> {
    List<ItensPedido> findByPedido_Id(Long pedidoId);
}
