package com.raizesnordeste.api.infrastructure.repository;

import com.raizesnordeste.api.domain.MovimentoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimentoEstoqueRepository extends JpaRepository<MovimentoEstoque, Long> {
}
