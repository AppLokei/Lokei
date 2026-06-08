package Lokei.aplication.infrastructure.shared;

import Lokei.aplication.infrastructure.shared.exception.AcessoNegadoException;
import Lokei.aplication.infrastructure.shared.exception.NaoAutenticadoException;
import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFound(RecursoNaoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarCorpo("NOT_FOUND", exception.getMessage()));
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<ApiErrorResponse> handleBusiness(RegraDeNegocioException exception) {
        return ResponseEntity.badRequest().body(criarCorpo("BUSINESS_RULE", exception.getMessage()));
    }

    @ExceptionHandler({NaoAutenticadoException.class})
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(NaoAutenticadoException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(criarCorpo("UNAUTHORIZED", exception.getMessage()));
    }

    @ExceptionHandler({AcessoNegadoException.class, AccessDeniedException.class})
    public ResponseEntity<ApiErrorResponse> handleForbidden(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(criarCorpo("FORBIDDEN", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException exception) {
        String mensagem = exception.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .distinct()
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(criarCorpo("VALIDATION", mensagem));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(criarCorpo("INTERNAL_ERROR", "Erro interno inesperado."));
    }

    private ApiErrorResponse criarCorpo(String codigo, String mensagem) {
        return new ApiErrorResponse(OffsetDateTime.now().toString(), codigo, mensagem);
    }
}
