package Lokei.aplication.application.dto;

public record SolicitarAluguelRequest(
        Integer usuarioId,
        String dataInicio,
        String dataFim
) {
}
