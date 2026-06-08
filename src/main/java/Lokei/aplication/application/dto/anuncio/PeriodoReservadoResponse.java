package Lokei.aplication.application.dto.anuncio;

public record PeriodoReservadoResponse(
        Integer aluguelId,
        String dataInicio,
        String dataFim,
        String status
) {
}
