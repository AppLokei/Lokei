package Lokei.aplication.application.dto;

import java.math.BigDecimal;

public record SolicitarAluguelResponse(
        Long aluguelId,
        Long anuncioId,
        Long locatarioId,
        String status,
        String dataInicio,
        String dataFim,
        long quantidadeDias,
        BigDecimal valorDiario,
        BigDecimal valorTotal,
        String mensagem
) {
}
