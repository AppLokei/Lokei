package Lokei.aplication.infrastructure.shared;

public record ApiErrorResponse(
        String timestamp,
        String codigo,
        String mensagem
) {
}
