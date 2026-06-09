package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Chat;
import Lokei.aplication.infrastructure.persistence.entities.ChatEntity;

public class ChatMapper {

    public static Chat toDomain(ChatEntity entity) {
        return new Chat(
                entity.getId(),
                entity.getDataCriacao(),
                entity.getLocatarioId(),
                entity.getLocadorId(),
                entity.getAnuncioId()
        );
    }

    public static ChatEntity toEntity(Chat chat) {
        ChatEntity entity = new ChatEntity();
        entity.setId(chat.getId());
        entity.setDataCriacao(chat.getDataCriacao());
        entity.setLocatarioId(chat.getLocatarioId());
        entity.setLocadorId(chat.getLocadorId());
        entity.setAnuncioId(chat.getAnuncioId());
        return entity;
    }
}
