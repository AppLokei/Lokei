package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Chat;

import java.util.List;
import java.util.Optional;

/**
 * Interface de porta de saída para persistência de Chats.
 */
public interface ChatGateway {
    Chat criarChat(Chat chat);
    Optional<Chat> buscarChatPorId(Long id);
    Optional<Chat> buscarChatPorAnuncioELocatario(Long anuncioId, Long locatarioId);
    List<Chat> buscarChatsPorUsuario(Long usuarioId);
}
