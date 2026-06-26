package com.raizesnordeste.api.application.service;

import com.raizesnordeste.api.api.dto.request.UnidadeRequest;
import com.raizesnordeste.api.api.dto.response.UnidadeResponse;
import com.raizesnordeste.api.application.exception.RecursoDuplicadoException;
import com.raizesnordeste.api.application.exception.RecursoNaoEncontradoException;
import com.raizesnordeste.api.domain.Unidade;
import com.raizesnordeste.api.infrastructure.repository.UnidadeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class UnidadeService {

    private final UnidadeRepository unidadeRepository;

    public List<UnidadeResponse> listarUnidades() {
        log.info("Iniciando listagem das unidades cadastradas");

        List<UnidadeResponse> unidadesResponse = unidadeRepository.findAll()
                .stream()
                .map(this::toUnidadeResponse)
                .toList();

        log.info("Listagem finalizada com sucesso");

        return unidadesResponse;
    }

    public UnidadeResponse buscarUnidadePorId(Long id) {
        log.info("Buscando unidade com ID: {}", id);

        Unidade unidadeEncontrada = unidadeRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Unidade não encontrada com ID {}", id);
                    return new RecursoNaoEncontradoException("Unidade não encontrada com ID: " + id);
                });

        log.info("Unidade encontrada com ID: {}", id);

        return toUnidadeResponse(unidadeEncontrada);
    }

    public UnidadeResponse cadastrarUnidade(UnidadeRequest unidadeRequest) {
        log.info("Iniciando cadastro de nova unidade");

        if(unidadeRepository.existsByNome(unidadeRequest.nome())) {
            log.warn("Tentativa de cadastro de unidade com nome já existente: {}", unidadeRequest.nome());
            throw new RecursoDuplicadoException(
                    "Já existe uma unidade cadastrada com o nome: " + unidadeRequest.nome());
        }

        Unidade novaUnidade = new Unidade();
        novaUnidade.setNome(unidadeRequest.nome());
        novaUnidade.setEndereco(unidadeRequest.endereco());

        Unidade unidadeSalva = unidadeRepository.save(novaUnidade);

        log.info("Nova unidade cadastrada com sucesso");

        return toUnidadeResponse(unidadeSalva);
    }

    public UnidadeResponse atualizarUnidade(Long id, UnidadeRequest unidadeRequest) {
        log.info("Iniciando atualização de unidade com ID: {}", id);

        Unidade unidade = unidadeRepository.findById(id)
                .orElseThrow(() -> {
                        log.warn("Unidade para atualização não encontrada com ID {}", id);
                        return new RecursoNaoEncontradoException("Unidade não encontrada com o ID: " + id);
                });

        if (unidadeRepository.existsByNomeAndIdNot(unidadeRequest.nome(), id)) {
            log.warn("Unidade com nome: {} já existe", unidadeRequest.nome());
            throw new RecursoDuplicadoException(
                    "Já existe outra unidade cadastrada com o nome: " + unidadeRequest.nome());
        }

        unidade.setNome(unidadeRequest.nome());
        unidade.setEndereco(unidadeRequest.endereco());

        unidadeRepository.save(unidade);

        log.info("Unidade atualizada com sucesso");

        return toUnidadeResponse(unidade);
    }

    private UnidadeResponse toUnidadeResponse(Unidade unidade) {
        return new UnidadeResponse(
                unidade.getId(),
                unidade.getNome(),
                unidade.getEndereco()
        );
    }
}
