package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.request.UnidadeRequest;
import com.raizesnordeste.api.api.dto.response.UnidadeResponse;
import com.raizesnordeste.api.application.service.UnidadeService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    private final UnidadeService unidadeService;

    @GetMapping
    public ResponseEntity<List<UnidadeResponse>> listar() {
        List<UnidadeResponse> unidades = unidadeService.listarUnidades();
        return ResponseEntity.ok(unidades);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadeResponse> getUnidade(@PathVariable Long id) {
        UnidadeResponse unidadeEncontrada = unidadeService.buscarUnidadePorId(id);
        return ResponseEntity.ok(unidadeEncontrada);
    }

    @PostMapping
    public ResponseEntity<UnidadeResponse> cadastrar(@RequestBody @Valid UnidadeRequest unidadeRequest) {
        UnidadeResponse unidadeSalva = unidadeService.cadastrarUnidade(unidadeRequest);
        return ResponseEntity.status(201).body(unidadeSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UnidadeResponse> atualizar(@PathVariable Long id,
                                                     @RequestBody @Valid UnidadeRequest unidadeRequest) {
        UnidadeResponse unidadeAtualizada = unidadeService.atualizarUnidade(id, unidadeRequest);
        return ResponseEntity.ok(unidadeAtualizada);
    }
}
