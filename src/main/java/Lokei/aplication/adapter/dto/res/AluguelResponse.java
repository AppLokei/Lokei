package Lokei.aplication.adapter.dto.res;

import Lokei.aplication.domain.enums.StatusAluguelEnum;

import java.math.BigDecimal;
import java.util.Date;

public record AluguelResponse(
        Long id,
        Date dataInicio,
        Date dataFim,
        BigDecimal valorTotal,
        StatusAluguelEnum status,
        Long anuncioId,
        Long locatarioId
) {
}

