package Lokei.aplication.application.dto.aluguel;

import java.math.BigDecimal;

public record SolicitarAluguelResponse(
        Integer aluguelId,
        Integer anuncioId,
        Integer locatarioId,
        String status,
        String dataInicio,
        String dataFim,
        long quantidadeDias,
        BigDecimal valorDiario,
        BigDecimal valorTotal,
        String mensagem
) {
}
