package com.raizesnordeste.api.infrastructure.repository;

import com.raizesnordeste.api.domain.CardapioItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardapioItemRepository extends JpaRepository<CardapioItem, Long> {
    List<CardapioItem> findByUnidadeId(Long unidadeId);
    boolean existsByUnidadeIdAndProdutoId(Long unidadeId, Long produtoId);
    boolean existsByUnidadeIdAndProdutoIdAndIdNot(Long unidadeId, Long produtoId, Long id);
    Optional<CardapioItem> findByUnidadeIdAndProdutoId(Long unidadeId, Long produtoId);
}
