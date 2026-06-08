package Lokei.aplication.application.dto.chat;

public record MensagemChatResponse(
        Integer id,
        Integer remetenteId,
        String remetente,
        String conteudo,
        String dataCriacao
) {
}
