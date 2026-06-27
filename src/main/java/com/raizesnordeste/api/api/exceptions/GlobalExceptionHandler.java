package com.raizesnordeste.api.api.exceptions;

import com.raizesnordeste.api.api.dto.response.ErroResponse;
import com.raizesnordeste.api.application.exception.EstoqueInsuficienteException;
import com.raizesnordeste.api.application.exception.RecursoDuplicadoException;
import com.raizesnordeste.api.application.exception.RecursoNaoEncontradoException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleException(HttpServletRequest request, Exception ex) {
        return ResponseEntity.status(500).body(
                new ErroResponse(
                        "ERRO_INTERNO",
                        ex.getMessage(),
                        LocalDateTime.now().toString(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> handleRecursoNaoEncontrado(HttpServletRequest request, RecursoNaoEncontradoException ex) {
        return ResponseEntity.status(404).body(
                new ErroResponse(
                        "RECURSO_NAO_ENCONTRADO",
                        ex.getMessage(),
                        LocalDateTime.now().toString(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(RecursoDuplicadoException.class)
    public ResponseEntity<ErroResponse> handleRecursoDuplicado(HttpServletRequest request, RecursoDuplicadoException ex) {
        return ResponseEntity.status(409).body(
                new ErroResponse(
                        "RECURSO_DUPLICADO",
                        ex.getMessage(),
                        LocalDateTime.now().toString(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<ErroResponse> handleEstoqueInsuficiente(HttpServletRequest request, EstoqueInsuficienteException ex) {
        return ResponseEntity.status(422).body(
                new ErroResponse(
                        "ESTOQUE_INSUFICIENTE",
                        ex.getMessage(),
                        LocalDateTime.now().toString(),
                        request.getRequestURI()
                ));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroResponse> handleIllegalState(
            IllegalStateException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse("OPERACAO_INVALIDA",
                        ex.getMessage(),
                        LocalDateTime.now().toString(),
                        request.getRequestURI()
                ));
    }
}
