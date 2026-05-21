package com.leonardo.debt_manager_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroValidacaoDTO> handleValidationErrors(MethodArgumentNotValidException ex) {


        List<ErroValidacaoDTO.CampoErro> erros = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(erro -> new ErroValidacaoDTO.CampoErro(erro.getField(), erro.getDefaultMessage()))
                .toList();

        ErroValidacaoDTO resposta = new ErroValidacaoDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), // 400
                "Erro de validação nos dados enviados",
                erros
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resposta);
    }
}