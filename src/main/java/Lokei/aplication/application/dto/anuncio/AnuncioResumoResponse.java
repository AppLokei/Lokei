package Lokei.aplication.application.dto.anuncio;

import java.math.BigDecimal;

public record AnuncioResumoResponse(
        Integer id,
        String titulo,
        String descricaoCurta,
        BigDecimal valorDiario,
        String categoria,
        String status,
        String cidade,
        String imagemPrincipalUrl
) {
}
