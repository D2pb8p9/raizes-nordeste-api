package com.raizesnordeste.api.application.service;

import com.raizesnordeste.api.api.dto.request.CardapioItemRequest;
import com.raizesnordeste.api.api.dto.response.CardapioItemResponse;
import com.raizesnordeste.api.application.exception.RecursoDuplicadoException;
import com.raizesnordeste.api.application.exception.RecursoNaoEncontradoException;
import com.raizesnordeste.api.domain.CardapioItem;
import com.raizesnordeste.api.domain.Produto;
import com.raizesnordeste.api.domain.Unidade;
import com.raizesnordeste.api.infrastructure.repository.CardapioItemRepository;
import com.raizesnordeste.api.infrastructure.repository.ProdutoRepository;
import com.raizesnordeste.api.infrastructure.repository.UnidadeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class CardapioItemService {

    private final CardapioItemRepository cardapioItemRepository;
    private final ProdutoRepository produtoRepository;
    private final UnidadeRepository unidadeRepository;

    public List<CardapioItemResponse> listarItensPorUnidade(Long unidadeId) {
        log.info("Iniciando listagem de items do cardápio da unidade com ID: {} ", unidadeId);

        List<CardapioItem> listaCardapioItem = cardapioItemRepository.findByUnidadeId(unidadeId);

        log.info("Listagem de items do cardápio da unidade com ID: {} finalizada com sucesso", unidadeId);

        return listaCardapioItem.stream()
                .map(cardapioItem -> toCardapioItemResponse(
                        cardapioItem,
                        cardapioItem.getProduto(),
                        cardapioItem.getUnidade()))
                .toList();
    }

    public CardapioItemResponse cadastrarItem(CardapioItemRequest cardapioItemRequest) {
        log.info("Iniciando cadastro de item do cardápio para o produto com ID: {} na unidade com ID: {}",
                cardapioItemRequest.produtoId(), cardapioItemRequest.unidadeId());

        Unidade unidade = buscarUnidadePorId(cardapioItemRequest.unidadeId());

        Produto produto = buscarProdutoPorId(cardapioItemRequest.produtoId());

        if (cardapioItemRepository.existsByUnidadeIdAndProdutoId(cardapioItemRequest.unidadeId(), cardapioItemRequest.produtoId())) {
            throw new RecursoDuplicadoException("Este produto já está cadastrado no cardápio desta unidade");
        }


        CardapioItem novoCardapioItem = new CardapioItem();

        novoCardapioItem.setUnidade(unidade);
        novoCardapioItem.setProduto(produto);
        novoCardapioItem.setPreco(cardapioItemRequest.preco());
        novoCardapioItem.setDisponivel(cardapioItemRequest.disponivel());

        cardapioItemRepository.save(novoCardapioItem);

        log.info("Item cadastrado com sucesso");

        return toCardapioItemResponse(novoCardapioItem, produto, unidade);
    }

    public CardapioItemResponse atualizarItem(CardapioItemRequest cardapioItemRequest, Long idItem) {
        log.info("Iniciando atualização de item do cardápio com ID: {}", idItem);

        CardapioItem cardapioItem = cardapioItemRepository.findById(idItem)
                .orElseThrow(() -> {
                    log.warn("Item do cardápio não encontrado com ID {}", idItem);
                    return new RecursoNaoEncontradoException("Item do cardápio não encontrado com ID: " + idItem);
                });

        if (cardapioItemRepository.existsByUnidadeIdAndProdutoIdAndIdNot(
                cardapioItemRequest.unidadeId(), cardapioItemRequest.produtoId(), idItem)) {
            log.warn("Já existe item do cardápio com o produto ID {} na unidade ID {}",
                    cardapioItemRequest.produtoId(), cardapioItemRequest.unidadeId());
            throw new RecursoDuplicadoException("Já exite item do cardápio com o produto ID: "
                    + cardapioItemRequest.produtoId() + " na unidade ID: " + cardapioItemRequest.unidadeId());
        }

        Unidade unidade = buscarUnidadePorId(cardapioItemRequest.unidadeId());
        Produto produto = buscarProdutoPorId(cardapioItemRequest.produtoId());

        cardapioItem.setUnidade(unidade);
        cardapioItem.setProduto(produto);
        cardapioItem.setPreco(cardapioItemRequest.preco());
        cardapioItem.setDisponivel(cardapioItemRequest.disponivel());

        cardapioItemRepository.save(cardapioItem);

        log.info("Item atualizado com sucesso");

        return toCardapioItemResponse(cardapioItem, produto, unidade);
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

    private CardapioItemResponse toCardapioItemResponse(CardapioItem cardapioItem, Produto produto, Unidade unidade) {
        return new CardapioItemResponse(
                cardapioItem.getId(),
                produto.getId(),
                cardapioItem.getProduto().getNome(),
                unidade.getId(),
                cardapioItem.getUnidade().getNome(),
                cardapioItem.getPreco(),
                cardapioItem.getDisponivel()
        );
    }
}
