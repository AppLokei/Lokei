package Lokei.aplication.application.usecases.chat;

import Lokei.aplication.domain.entities.Chat;
import Lokei.aplication.domain.exceptions.UsuarioNotFoundException;
import Lokei.aplication.domain.gateways.ChatGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;

import java.util.List;

/**
 * Caso de uso: Lista todos os chats de um usuário (locador ou locatário).
 */
public class ListarChatsPorUsuarioUseCase {

    private final ChatGateway chatGateway;
    private final UsuarioGateway usuarioGateway;

    public ListarChatsPorUsuarioUseCase(ChatGateway chatGateway, UsuarioGateway usuarioGateway) {
        this.chatGateway = chatGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public List<Chat> execute(Long usuarioId) {
        usuarioGateway.buscarUsuarioPorId(usuarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(usuarioId));

        return chatGateway.buscarChatsPorUsuario(usuarioId);
    }
}
