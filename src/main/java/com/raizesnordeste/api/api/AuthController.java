package com.raizesnordeste.api.api;

import com.raizesnordeste.api.api.dto.request.LoginRequest;
import com.raizesnordeste.api.api.dto.request.RegistroRequest;
import com.raizesnordeste.api.application.service.AuthService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody @Valid RegistroRequest request) {
        authService.registrar(request);
        return ResponseEntity.status(201).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> executarLogin(@RequestBody @Valid LoginRequest request) {
        String token = authService.executarLogin(request);
        return ResponseEntity.status(200).body(Map.of("token", token));
    }
}
