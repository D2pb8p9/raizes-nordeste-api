package com.raizesnordeste.api.application.service;

import com.raizesnordeste.api.api.dto.request.EntradaEstoqueRequest;
import com.raizesnordeste.api.api.dto.response.EstoqueResponse;
import com.raizesnordeste.api.application.exception.EstoqueInsuficienteException;
import com.raizesnordeste.api.application.exception.RecursoNaoEncontradoException;
import com.raizesnordeste.api.domain.Estoque;
import com.raizesnordeste.api.domain.MovimentoEstoque;
import com.raizesnordeste.api.domain.Produto;
import com.raizesnordeste.api.domain.Unidade;
import com.raizesnordeste.api.domain.enums.TipoMovimentoEstoque;
import com.raizesnordeste.api.infrastructure.repository.EstoqueRepository;
import com.raizesnordeste.api.infrastructure.repository.MovimentoEstoqueRepository;
import com.raizesnordeste.api.infrastructure.repository.ProdutoRepository;
import com.raizesnordeste.api.infrastructure.repository.UnidadeRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final MovimentoEstoqueRepository movimentoEstoqueRepository;
    private final UnidadeRepository unidadeRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueResponse registrarEntrada(EntradaEstoqueRequest request) {
        log.info("Iniciando entrada de estoque para produto ID: {} na unidade ID: {}",
                request.produtoId(), request.unidadeId());

        Unidade unidade = buscarUnidadePorId(request.unidadeId());
        Produto produto = buscarProdutoPorId(request.produtoId());

        Estoque estoque = estoqueRepository
                .findByUnidadeIdAndProdutoId(request.unidadeId(), request.produtoId())
                .orElseGet(() -> {
                    Estoque novo = new Estoque();
                    novo.setUnidade(unidade);
                    novo.setProduto(produto);
                    novo.setQuantidadeAtual(0);
                    return novo;
                });

        estoque.setQuantidadeAtual(estoque.getQuantidadeAtual() + request.quantidade());
        estoqueRepository.save(estoque);

        MovimentoEstoque movimento = new MovimentoEstoque();
        movimento.setUnidade(unidade);
        movimento.setProduto(produto);
        movimento.setTipoMovimento(TipoMovimentoEstoque.ENTRADA);
        movimento.setQuantidade(request.quantidade());
        movimento.setDataMovimento(LocalDateTime.now());
        movimentoEstoqueRepository.save(movimento);

        log.info("Entrada de estoque registrada com sucesso. Novo saldo: {}", estoque.getQuantidadeAtual());

        return toEstoqueResponse(estoque);
    }

    public void registrarSaida(Long unidadeId, Long produtoId, Integer quantidade) {
        log.info("Iniciando saída de estoque para produto ID: {} na unidade ID: {}", produtoId, unidadeId);

        Estoque estoque = estoqueRepository
                .findByUnidadeIdAndProdutoId(unidadeId, produtoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Estoque não encontrado para produto ID: " + produtoId + " na unidade ID: " + unidadeId));

        if (estoque.getQuantidadeAtual() < quantidade) {
            log.warn("Estoque insuficiente. Disponível: {}, solicitado: {}", estoque.getQuantidadeAtual(), quantidade);
            throw new EstoqueInsuficienteException(
                    "Estoque insuficiente para o produto ID: " + produtoId +
                            ". Disponível: " + estoque.getQuantidadeAtual() + ", solicitado: " + quantidade);
        }

        estoque.setQuantidadeAtual(estoque.getQuantidadeAtual() - quantidade);
        estoqueRepository.save(estoque);

        log.info("Saída de estoque registrada com sucesso. Novo saldo: {}", estoque.getQuantidadeAtual());
    }

    public List<EstoqueResponse> consultarPorUnidade(Long unidadeId) {
        log.info("Iniciando consulta de estoque da unidade ID: {}", unidadeId);

        buscarUnidadePorId(unidadeId);

        List<EstoqueResponse> estoques = estoqueRepository.findByUnidadeId(unidadeId)
                .stream()
                .map(this::toEstoqueResponse)
                .toList();

        log.info("Consulta de estoque da unidade ID: {} finalizada com sucesso", unidadeId);

        return estoques;
    }

    private Unidade buscarUnidadePorId(Long unidadeId) {
        return unidadeRepository.findById(unidadeId)
                .orElseThrow(() -> {
                    log.warn("Unidade não encontrada com ID {}", unidadeId);
                    return new RecursoNaoEncontradoException("Unidade não encontrada com ID: " + unidadeId);
                });
    }

    private Produto buscarProdutoPorId(Long produtoId) {
        return produtoRepository.findById(produtoId)
                .orElseThrow(() -> {
                    log.warn("Produto não encontrado com ID {}", produtoId);
                    return new RecursoNaoEncontradoException("Produto não encontrado com ID: " + produtoId);
                });
    }

    private EstoqueResponse toEstoqueResponse(Estoque estoque) {
        return new EstoqueResponse(
                estoque.getProduto().getId(),
                estoque.getProduto().getNome(),
                estoque.getQuantidadeAtual()
        );
    }
}
