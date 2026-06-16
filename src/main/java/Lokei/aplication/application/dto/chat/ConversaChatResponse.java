package Lokei.aplication.application.dto.chat;

import java.util.List;

public record ConversaChatResponse(
        Integer id,
        Integer aluguelId,
        Integer anuncioId,
        String tituloAnuncio,
        String imagemPrincipalUrl,
        String locador,
        String locatario,
        List<MensagemChatResponse> mensagens
) {
}
