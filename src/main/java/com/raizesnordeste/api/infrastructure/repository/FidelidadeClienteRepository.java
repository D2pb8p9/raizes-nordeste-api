package com.raizesnordeste.api.infrastructure.repository;

import com.raizesnordeste.api.domain.FidelidadeCliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FidelidadeClienteRepository extends JpaRepository<FidelidadeCliente, Long> {
}
