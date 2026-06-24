package com.raizesnordeste.api.infrastructure.repository;

import com.raizesnordeste.api.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}
