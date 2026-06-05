package Lokei.aplication.adapter.exception;

import Lokei.aplication.domain.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> response (HttpStatus status, String mensagem) {
        return ResponseEntity.status(status).body(new ErrorResponse(mensagem));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        return response(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Handler Anuncio

    @ExceptionHandler(AnuncioInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleAnuncioInvalido(AnuncioInvalidoException ex) {
        return response(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Handler ferramenta

    @ExceptionHandler(Lokei.aplication.domain.exceptions.FerramentaInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleFerramentaInvalida(FerramentaInvalidaException ex) {
        return response(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    // Handler imagem

    @ExceptionHandler(ImagemInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleImagemInvalida(ImagemInvalidaException ex) {
        return response(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        return response(HttpStatus.BAD_REQUEST, "A imagem excede o tamanho máximo permitido de 5MB.");
    }

    // Handler usuario

    @ExceptionHandler(UsuarioNaoAutorizadoException.class)
    public ResponseEntity<ErrorResponse> handleNaoAutorizado(UsuarioNaoAutorizadoException ex) {
        return response(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    // Request

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ErrorResponse> handleMissingPartException(MissingServletRequestPartException ex) {
        return response(HttpStatus.BAD_REQUEST, "O campo obrigatório '" + ex.getRequestPartName() + "' não foi enviado.");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return response(HttpStatus.BAD_REQUEST, "Valor inválido para o parâmetro '" + ex.getName() + "': " + ex.getValue());
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
