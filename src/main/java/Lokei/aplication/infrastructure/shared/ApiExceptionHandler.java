package Lokei.aplication.infrastructure.shared;

import Lokei.aplication.infrastructure.shared.exception.RegraDeNegocioException;
import Lokei.aplication.infrastructure.shared.exception.RecursoNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(RecursoNaoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(criarCorpo("NOT_FOUND", exception.getMessage()));
    }

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleBusiness(RegraDeNegocioException exception) {
        return ResponseEntity.badRequest().body(criarCorpo("BUSINESS_RULE", exception.getMessage()));
    }

    private Map<String, Object> criarCorpo(String codigo, String mensagem) {
        return Map.of(
                "timestamp", OffsetDateTime.now().toString(),
                "codigo", codigo,
                "mensagem", mensagem
        );
    }
}
