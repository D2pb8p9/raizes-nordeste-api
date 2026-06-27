package com.raizesnordeste.api.infrastructure.repository;

import com.raizesnordeste.api.domain.FidelidadeCliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FidelidadeClienteRepository extends JpaRepository<FidelidadeCliente, Long> {
    Optional<FidelidadeCliente> findByUsuario_Id(Long usuarioId);
    Optional<FidelidadeCliente> findByUsuario_Email(String email);
}
