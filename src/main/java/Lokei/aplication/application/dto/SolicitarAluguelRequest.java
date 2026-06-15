package Lokei.aplication.application.dto;

public record SolicitarAluguelRequest(
        Long usuarioId,
        String dataInicio,
        String dataFim
) {
}
