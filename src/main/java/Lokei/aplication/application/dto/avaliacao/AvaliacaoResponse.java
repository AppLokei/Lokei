package Lokei.aplication.application.dto.avaliacao;

public record AvaliacaoResponse(
        Integer id,
        Integer nota,
        String comentario,
        String tipo,
        String autor,
        String alvo,
        String dataCriacao
) {
}
