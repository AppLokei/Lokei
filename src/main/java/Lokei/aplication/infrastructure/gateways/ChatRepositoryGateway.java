package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.Chat;
import Lokei.aplication.domain.gateways.ChatGateway;
import Lokei.aplication.infrastructure.persistence.entities.ChatEntity;
import Lokei.aplication.infrastructure.persistence.mapper.ChatMapper;
import Lokei.aplication.infrastructure.persistence.repository.ChatRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ChatRepositoryGateway implements ChatGateway {

    private final ChatRepository chatRepository;

    public ChatRepositoryGateway(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public Chat criarChat(Chat chat) {
        ChatEntity entity = ChatMapper.toEntity(chat);
        ChatEntity salvo = chatRepository.save(entity);
        return ChatMapper.toDomain(salvo);
    }

    @Override
    public Optional<Chat> buscarChatPorId(Long id) {
        return chatRepository.findById(id).map(ChatMapper::toDomain);
    }

    @Override
    public Optional<Chat> buscarChatPorAnuncioELocatario(Long anuncioId, Long locatarioId) {
        return chatRepository.findByAnuncio_IdAndLocatario_Id(anuncioId, locatarioId)
                .map(ChatMapper::toDomain);
    }

    @Override
    public List<Chat> buscarChatsPorUsuario(Long usuarioId) {
        return chatRepository.findAllByUsuarioId(usuarioId)
                .stream().map(ChatMapper::toDomain).toList();
    }
}
