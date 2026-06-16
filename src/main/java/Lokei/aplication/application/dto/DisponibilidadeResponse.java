package Lokei.aplication.application.dto;

import java.util.List;

public record DisponibilidadeResponse(
        Integer anuncioId,
        String statusAnuncio,
        boolean disponivelParaNovasReservas,
        List<PeriodoReservadoResponse> periodosReservados
) {
}
