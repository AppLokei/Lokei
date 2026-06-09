package Lokei.aplication.adapter.mapper;

import Lokei.aplication.adapter.dto.res.ChatResponse;
import Lokei.aplication.adapter.dto.res.MensagemResponse;
import Lokei.aplication.domain.entities.Chat;
import Lokei.aplication.domain.entities.Mensagem;

public class ChatControllerMapper {

    public static ChatResponse toChatResponse(Chat chat) {
        return new ChatResponse(
                chat.getId(),
                chat.getDataCriacao(),
                chat.getLocatarioId(),
                chat.getLocadorId(),
                chat.getAnuncioId()
        );
    }

    public static MensagemResponse toMensagemResponse(Mensagem mensagem) {
        return new MensagemResponse(
                mensagem.getId(),
                mensagem.getConteudo(),
                mensagem.getDataHoraEnvio(),
                mensagem.isLida(),
                mensagem.getRemetenteId(),
                mensagem.getChatId()
        );
    }
}
