package Lokei.aplication.application.dto;

import java.math.BigDecimal;
import java.util.List;

public record AnuncioDetalheResponse(
        Integer id,
        String titulo,
        String descricao,
        BigDecimal valorDiario,
        String categoria,
        String status,
        ProprietarioResumo proprietario,
        List<String> imagens,
        List<AvaliacaoResumo> avaliacoes,
        BigDecimal notaMedia,
        Integer totalAvaliacoes,
        String acaoPrimaria,
        boolean disponivelParaReserva
) {

    public record ProprietarioResumo(
            Integer id,
            String nome,
            String emailMascarado,
            String telefoneMascarado
    ) {
    }

    public record AvaliacaoResumo(
            Integer id,
            Integer nota,
            String comentario,
            String dataCriacao
    ) {
    }
}
