package com.raizesnordeste.api.application.service;

import com.raizesnordeste.api.api.dto.request.ProcessarPagamentoRequest;
import com.raizesnordeste.api.api.dto.response.PagamentoResponse;
import com.raizesnordeste.api.application.exception.RecursoNaoEncontradoException;
import com.raizesnordeste.api.domain.Pagamento;
import com.raizesnordeste.api.domain.Pedido;
import com.raizesnordeste.api.domain.enums.StatusPagamento;
import com.raizesnordeste.api.infrastructure.repository.PagamentoRepository;
import com.raizesnordeste.api.infrastructure.repository.PedidoRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;
    private final FidelidadeService fidelidadeService;

    @Transactional
    public PagamentoResponse processarPagamento(ProcessarPagamentoRequest request) {
        log.info("Processando pagamento para o pedido ID: {}", request.pedidoId());

        Pedido pedido = pedidoRepository.findById(request.pedidoId())
                .orElseThrow(() -> {
                    log.warn("Pedido não encontrado com ID {}", request.pedidoId());
                    return new RecursoNaoEncontradoException("Pedido não encontrado com ID: " + request.pedidoId());
                });

        if (pedido.getStatusPagamento() == StatusPagamento.APROVADO) {
            throw new IllegalStateException("O pedido ID: " + request.pedidoId() + " já possui pagamento aprovado.");
        }

        StatusPagamento resultado = simularGatewayPagamento();
        log.info("Resultado do gateway (mock) para pedido ID: {}: {}", request.pedidoId(), resultado);

        Pagamento pagamento = new Pagamento();
        pagamento.setPedido(pedido);
        pagamento.setTipoPagamento(request.tipoPagamento());
        pagamento.setStatusPagamento(resultado);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamentoRepository.save(pagamento);

        pedido.setStatusPagamento(resultado);
        pedidoRepository.save(pedido);

        if (resultado == StatusPagamento.APROVADO) {
            fidelidadeService.acumularPontos(pedido.getUsuario().getId(), pedido.getValorTotal());
        }

        log.info("Pagamento registrado com sucesso para o pedido ID: {}", request.pedidoId());

        return new PagamentoResponse(
                pagamento.getId(),
                pedido.getId(),
                pagamento.getTipoPagamento(),
                pagamento.getStatusPagamento(),
                pagamento.getDataPagamento()
        );
    }

    // Simula a resposta de um gateway externo — em produção seria uma chamada HTTP real
    private StatusPagamento simularGatewayPagamento() {
        return Math.random() < 0.8 ? StatusPagamento.APROVADO : StatusPagamento.RECUSADO;
    }
}

