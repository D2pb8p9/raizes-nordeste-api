package com.raizesnordeste.api.application.service;

import com.raizesnordeste.api.api.dto.request.AtualizarStatusPedidoRequest;
import com.raizesnordeste.api.api.dto.request.CriarPedidoRequest;
import com.raizesnordeste.api.api.dto.request.ItemPedidoRequest;
import com.raizesnordeste.api.api.dto.response.ItemPedidoResponse;
import com.raizesnordeste.api.api.dto.response.PedidoResponse;
import com.raizesnordeste.api.application.exception.RecursoNaoEncontradoException;
import com.raizesnordeste.api.domain.CardapioItem;
import com.raizesnordeste.api.domain.ItensPedido;
import com.raizesnordeste.api.domain.Pedido;
import com.raizesnordeste.api.domain.Unidade;
import com.raizesnordeste.api.domain.Usuario;
import com.raizesnordeste.api.domain.enums.CanalPedido;
import com.raizesnordeste.api.domain.enums.StatusPagamento;
import com.raizesnordeste.api.domain.enums.StatusPedido;
import com.raizesnordeste.api.infrastructure.repository.CardapioItemRepository;
import com.raizesnordeste.api.infrastructure.repository.ItensPedidoRepository;
import com.raizesnordeste.api.infrastructure.repository.PedidoRepository;
import com.raizesnordeste.api.infrastructure.repository.UnidadeRepository;
import com.raizesnordeste.api.infrastructure.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItensPedidoRepository itensPedidoRepository;
    private final UnidadeRepository unidadeRepository;
    private final UsuarioRepository usuarioRepository;
    private final CardapioItemRepository cardapioItemRepository;
    private final EstoqueService estoqueService;

    @Transactional
    public PedidoResponse criarPedido(CriarPedidoRequest request) {
        log.info("Iniciando criação de pedido para usuário ID: {} na unidade ID: {}",
                request.usuarioId(), request.unidadeId());

        Unidade unidade = buscarUnidadePorId(request.unidadeId());
        Usuario usuario = buscarUsuarioPorId(request.usuarioId());

        Pedido pedido = new Pedido();
        pedido.setUnidade(unidade);
        pedido.setUsuario(usuario);
        pedido.setCanalPedido(request.canalPedido());
        pedido.setStatus(StatusPedido.RECEBIDO);
        pedido.setStatusPagamento(StatusPagamento.PENDENTE);
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setValorTotal(BigDecimal.ZERO);
        pedidoRepository.save(pedido);

        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemPedidoRequest itemRequest : request.itens()) {
            CardapioItem cardapioItem = cardapioItemRepository
                    .findByUnidadeIdAndProdutoId(request.unidadeId(), itemRequest.produtoId())
                    .orElseThrow(() -> {
                        log.warn("Produto ID: {} não encontrado no cardápio da unidade ID: {}",
                                itemRequest.produtoId(), request.unidadeId());
                        return new RecursoNaoEncontradoException(
                                "Produto ID: " + itemRequest.produtoId() + " não encontrado no cardápio desta unidade");
                    });

            estoqueService.registrarSaida(request.unidadeId(), itemRequest.produtoId(), itemRequest.quantidade());

            ItensPedido item = new ItensPedido();
            item.setPedido(pedido);
            item.setProduto(cardapioItem.getProduto());
            item.setQuantidade(itemRequest.quantidade());
            item.setPrecoUnitario(cardapioItem.getPreco());
            item.setObservacao(itemRequest.observacao());
            itensPedidoRepository.save(item);

            valorTotal = valorTotal.add(cardapioItem.getPreco()
                    .multiply(BigDecimal.valueOf(itemRequest.quantidade())));
        }

        pedido.setValorTotal(valorTotal);
        pedidoRepository.save(pedido);

        log.info("Pedido ID: {} criado com sucesso. Valor total: {}", pedido.getId(), valorTotal);

        Pedido pedidoAtualizado = pedidoRepository.findById(pedido.getId()).orElseThrow();
        return toPedidoResponse(pedidoAtualizado);
    }

    public List<PedidoResponse> consultarPedidos(Long unidadeId, StatusPedido status, CanalPedido canalPedido) {
        log.info("Consultando pedidos da unidade ID: {}", unidadeId);

        buscarUnidadePorId(unidadeId);

        return pedidoRepository.findByFiltros(unidadeId, status, canalPedido)
                .stream()
                .map(this::toPedidoResponse)
                .toList();
    }

    public PedidoResponse atualizarStatus(Long pedidoId, AtualizarStatusPedidoRequest request) {
        log.info("Atualizando status do pedido ID: {} para {}", pedidoId, request.status());

        Pedido pedido = buscarPedidoPorId(pedidoId);

        validarTransicaoStatus(pedido.getStatus(), request.status());

        pedido.setStatus(request.status());
        pedidoRepository.save(pedido);

        log.info("Status do pedido ID: {} atualizado com sucesso para {}", pedidoId, request.status());

        return toPedidoResponse(pedido);
    }

    public PedidoResponse cancelarPedido(Long pedidoId) {
        log.info("Iniciando cancelamento do pedido ID: {}", pedidoId);

        Pedido pedido = buscarPedidoPorId(pedidoId);

        if (pedido.getStatus() == StatusPedido.PRONTO || pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new IllegalStateException(
                    "Pedido ID: " + pedidoId + " não pode ser cancelado com status: " + pedido.getStatus());
        }

        for (ItensPedido item : pedido.getItens()) {
            estoqueService.registrarSaida(
                    pedido.getUnidade().getId(),
                    item.getProduto().getId(),
                    item.getQuantidade() * -1
            );
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);

        log.info("Pedido ID: {} cancelado com sucesso", pedidoId);

        return toPedidoResponse(pedido);
    }

    private void validarTransicaoStatus(StatusPedido atual, StatusPedido novo) {
        boolean valido = switch (atual) {
            case RECEBIDO -> novo == StatusPedido.EM_PREPARO;
            case EM_PREPARO -> novo == StatusPedido.PRONTO;
            case PRONTO -> novo == StatusPedido.ENTREGUE;
            default -> false;
        };

        if (!valido) {
            throw new IllegalStateException(
                    "Transição de status inválida: " + atual + " → " + novo);
        }
    }

    private Unidade buscarUnidadePorId(Long unidadeId) {
        return unidadeRepository.findById(unidadeId)
                .orElseThrow(() -> {
                    log.warn("Unidade não encontrada com ID {}", unidadeId);
                    return new RecursoNaoEncontradoException("Unidade não encontrada com ID: " + unidadeId);
                });
    }

    private Usuario buscarUsuarioPorId(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado com ID {}", usuarioId);
                    return new RecursoNaoEncontradoException("Usuário não encontrado com ID: " + usuarioId);
                });
    }

    private Pedido buscarPedidoPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> {
                    log.warn("Pedido não encontrado com ID {}", pedidoId);
                    return new RecursoNaoEncontradoException("Pedido não encontrado com ID: " + pedidoId);
                });
    }

    private PedidoResponse toPedidoResponse(Pedido pedido) {
        List<ItemPedidoResponse> itens = pedido.getItens() == null ? List.of() :
                pedido.getItens().stream()
                        .map(item -> new ItemPedidoResponse(
                                item.getProduto().getId(),
                                item.getProduto().getNome(),
                                item.getQuantidade(),
                                item.getPrecoUnitario(),
                                item.getObservacao()
                        ))
                        .toList();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getUnidade().getId(),
                pedido.getUnidade().getNome(),
                pedido.getUsuario().getId(),
                pedido.getUsuario().getNome(),
                pedido.getCanalPedido(),
                pedido.getStatus(),
                pedido.getStatusPagamento(),
                pedido.getDataPedido(),
                pedido.getValorTotal(),
                itens
        );
    }
}

