package com.raizesnordeste.api.infrastructure.repository;

import com.raizesnordeste.api.domain.Estoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
}
