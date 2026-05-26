package Lokei.aplication.adapter.dto.res;

import Lokei.aplication.domain.enums.StatusAnuncioEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record AnuncioResponse(
        Long id,
        String titulo,
        String descricao,
        BigDecimal valorDiario,
        LocalDateTime dataCriacao,
        StatusAnuncioEnum status,
        List<String> imagens
) {
}
