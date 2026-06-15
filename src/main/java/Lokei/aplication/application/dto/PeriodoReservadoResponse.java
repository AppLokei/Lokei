package Lokei.aplication.application.dto;

public record PeriodoReservadoResponse(
        Long aluguelId,
        String dataInicio,
        String dataFim,
        String status
) {
}
