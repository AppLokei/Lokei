package Lokei.aplication.adapter.dto.res;

public record NotificacaoResponse(
        Integer id,
        String titulo,
        String mensagem,
        boolean lida,
        String dataCriacao
) {
}
