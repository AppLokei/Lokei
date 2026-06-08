package Lokei.aplication.application.dto.aluguel;

import java.math.BigDecimal;

public record AluguelDetalheResponse(
        Integer id,
        Integer anuncioId,
        String tituloAnuncio,
        String descricaoAnuncio,
        String imagemPrincipalUrl,
        String locatario,
        String proprietario,
        String dataInicio,
        String dataFim,
        BigDecimal valorTotal,
        String status,
        boolean cancelavel,
        boolean chatDisponivel,
        boolean podeAvaliarAnuncio,
        boolean podeAvaliarContraparte,
        String motivoReprovacao,
        String motivoCancelamento
) {
}
