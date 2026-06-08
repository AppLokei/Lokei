package Lokei.aplication.application.dto.anuncio;

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
        List<Integer> imagemIds,
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
            String telefoneMascarado,
            String cidade
    ) {
    }

    public record AvaliacaoResumo(
            Integer id,
            Integer nota,
            String comentario,
            String autor,
            String dataCriacao
    ) {
    }
}
