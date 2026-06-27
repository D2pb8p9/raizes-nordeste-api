package com.raizesnordeste.api.application.service;

import com.raizesnordeste.api.api.dto.response.FidelidadeResponse;
import com.raizesnordeste.api.application.exception.RecursoDuplicadoException;
import com.raizesnordeste.api.application.exception.RecursoNaoEncontradoException;
import com.raizesnordeste.api.domain.FidelidadeCliente;
import com.raizesnordeste.api.domain.Usuario;
import com.raizesnordeste.api.infrastructure.repository.FidelidadeClienteRepository;
import com.raizesnordeste.api.infrastructure.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FidelidadeService {

    private final FidelidadeClienteRepository fidelidadeClienteRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public FidelidadeResponse aceitarConsentimento(String email) {
        log.info("Registrando consentimento de fidelidade para usuário com email: {}", email);

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Usuário não encontrado com email: {}", email);
                    return new RecursoNaoEncontradoException("Usuário não encontrado com email: " + email);
                });

        fidelidadeClienteRepository.findByUsuario_Email(email).ifPresent(f -> {
            throw new RecursoDuplicadoException("Usuário com email: " + email
                    + " já possui cadastro no programa de fidelidade.");
        });

        FidelidadeCliente fidelidade = new FidelidadeCliente();
        fidelidade.setUsuario(usuario);
        fidelidade.setPontos(0);
        fidelidade.setConsentimentoAceito(true);
        fidelidade.setDataConsentimento(LocalDateTime.now());
        fidelidadeClienteRepository.save(fidelidade);

        log.info("Consentimento registrado com sucesso para usuário com email: {}", email);

        return toResponse(fidelidade);
    }

    public FidelidadeResponse consultarMeusPontos(String email) {
        log.info("Consultando pontos de fidelidade para usuário com email: {}", email);

        FidelidadeCliente fidelidade = fidelidadeClienteRepository.findByUsuario_Email(email)
                .orElseThrow(() -> {
                    log.warn("Cadastro de fidelidade não encontrado para email: {}", email);
                    return new RecursoNaoEncontradoException("Cadastro de fidelidade não encontrado para o usuário.");
                });

        return toResponse(fidelidade);
    }


    @Transactional
    public void acumularPontos(Long usuarioId, BigDecimal valorTotal) {
        fidelidadeClienteRepository.findByUsuario_Id(usuarioId).ifPresent(fidelidade -> {
            if (Boolean.TRUE.equals(fidelidade.getConsentimentoAceito())) {
                int pontosGanhos = valorTotal.divideToIntegralValue(BigDecimal.TEN).intValue();
                if (pontosGanhos > 0) {
                    fidelidade.setPontos(fidelidade.getPontos() + pontosGanhos);
                    fidelidadeClienteRepository.save(fidelidade);
                    log.info("Usuário ID: {} acumulou {} pontos. Total: {}", usuarioId, pontosGanhos, fidelidade.getPontos());
                }
            }
        });
    }

    private FidelidadeResponse toResponse(FidelidadeCliente fidelidade) {
        return new FidelidadeResponse(
                fidelidade.getId(),
                fidelidade.getUsuario().getId(),
                fidelidade.getUsuario().getNome(),
                fidelidade.getPontos(),
                fidelidade.getConsentimentoAceito(),
                fidelidade.getDataConsentimento()
        );
    }
}
