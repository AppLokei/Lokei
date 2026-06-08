package Lokei.aplication.application.dto.aluguel;

import java.math.BigDecimal;

public record AluguelResumoResponse(
        Integer id,
        Integer anuncioId,
        String tituloAnuncio,
        String imagemPrincipalUrl,
        String locatario,
        String proprietario,
        String dataInicio,
        String dataFim,
        BigDecimal valorTotal,
        String status
) {
}
