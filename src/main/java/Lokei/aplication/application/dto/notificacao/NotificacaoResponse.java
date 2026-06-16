package Lokei.aplication.application.dto.notificacao;

public record NotificacaoResponse(
        Integer id,
        String tipo,
        String titulo,
        String mensagem,
        boolean lida,
        String dataCriacao
) {
}
