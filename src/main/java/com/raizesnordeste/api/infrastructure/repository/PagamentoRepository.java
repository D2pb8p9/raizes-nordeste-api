package com.raizesnordeste.api.infrastructure.repository;

import com.raizesnordeste.api.domain.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
