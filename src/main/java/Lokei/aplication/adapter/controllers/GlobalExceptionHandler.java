package Lokei.aplication.adapter.controllers;

import Lokei.aplication.domain.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handler anuncio

    @ExceptionHandler(AnuncioInvalidoException.class)
    public ResponseEntity<Map<String, String>> handleAnuncioInvalido(AnuncioInvalidoException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AnuncioNotFoundException.class)
    public ResponseEntity<String> handleAnuncioNotFound(AnuncioNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    // Handler ferramenta

    @ExceptionHandler(Lokei.aplication.domain.exceptions.FerramentaInvalidaException.class)
    public ResponseEntity<Map<String, String>> handleFerramentaInvalida(Lokei.aplication.domain.exceptions.FerramentaInvalidaException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Handler imagem

    @ExceptionHandler(ImagemNotFoundException.class)
    public ResponseEntity<String> handleImagemNotFound(ImagemNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ImagemInvalidaException.class)
    public ResponseEntity<Map<String, String>> handleImagemInvalida(ImagemInvalidaException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, String>> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "A imagem excede o tamanho máximo permitido de 5MB.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    // Handler usuario

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<String> handleUsuarioNotFound(UsuarioNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UsuarioNaoAutorizadoException.class)
    public ResponseEntity<String> handleNaoAutorizado(UsuarioNaoAutorizadoException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    // Outros Handlers

    @ExceptionHandler(org.springframework.web.multipart.support.MissingServletRequestPartException.class)
    public ResponseEntity<Map<String, String>> handleMissingPartException(org.springframework.web.multipart.support.MissingServletRequestPartException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "O campo obrigatório '" + ex.getRequestPartName() + "' não foi enviado.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("erro", "Valor inválido para o parâmetro '" + ex.getName() + "': " + ex.getValue());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

}
