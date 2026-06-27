package com.raizesnordeste.api.api.controller;

import com.raizesnordeste.api.api.dto.response.FidelidadeResponse;
import com.raizesnordeste.api.application.service.FidelidadeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/fidelidade")
public class FidelidadeController {

    private final FidelidadeService fidelidadeService;

    @GetMapping("/meus-pontos")
    public ResponseEntity<FidelidadeResponse> consultarMeusPontos(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(fidelidadeService.consultarMeusPontos(email));
    }


    @PostMapping("/consentimento")
    public ResponseEntity<FidelidadeResponse> aceitarConsentimento(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fidelidadeService.aceitarConsentimento(email));
    }

}

