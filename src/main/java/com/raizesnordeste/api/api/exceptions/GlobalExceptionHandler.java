package com.raizesnordeste.api.api.exceptions;

import com.raizesnordeste.api.api.dto.ErroResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleException(HttpServletRequest request, Exception ex) {
        return ResponseEntity.status(400).body(
                new ErroResponse(
                        "ERRO_INTERNO",
                        ex.getMessage(),
                        LocalDateTime.now().toString(),
                        request.getRequestURI()
                ));
    }
}
