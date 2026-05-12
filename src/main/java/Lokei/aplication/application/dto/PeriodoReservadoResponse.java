package Lokei.aplication.application.dto;

public record PeriodoReservadoResponse(
        Integer aluguelId,
        String dataInicio,
        String dataFim,
        String status
) {
}
