package Lokei.aplication.application.usecases.chat;

import Lokei.aplication.domain.entities.Chat;
import Lokei.aplication.domain.entities.Mensagem;
import Lokei.aplication.domain.exceptions.ChatNotFoundException;
import Lokei.aplication.domain.exceptions.UsuarioNaoAutorizadoException;
import Lokei.aplication.domain.gateways.ChatGateway;
import Lokei.aplication.domain.gateways.MensagemGateway;

import java.util.List;

/**
 * Caso de uso: Listar mensagens de um chat.
 * RN-011: Apenas locador e locatário podem visualizar as mensagens.
 * Ao listar, as mensagens não lidas do destinatário são marcadas como lidas.
 */
public class ListarMensagensUseCase {

    private final MensagemGateway mensagemGateway;
    private final ChatGateway chatGateway;

    public ListarMensagensUseCase(MensagemGateway mensagemGateway, ChatGateway chatGateway) {
        this.mensagemGateway = mensagemGateway;
        this.chatGateway = chatGateway;
    }

    /**
     * @param chatId      ID do chat
     * @param usuarioId   ID do usuário solicitante (para verificar autorização e marcar lidas)
     * @return            Lista de mensagens ordenadas por data
     */
    public List<Mensagem> execute(Long chatId, Long usuarioId) {

        Chat chat = chatGateway.buscarChatPorId(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));

        // RN-011: apenas partes do chat podem ver as mensagens
        if (!chat.usuarioPodeAcessar(usuarioId)) {
            throw new UsuarioNaoAutorizadoException(
                    "Apenas as partes envolvidas no anúncio podem visualizar as mensagens deste chat.");
        }

        // Marca mensagens não lidas do outro participante como lidas
        mensagemGateway.marcarMensagensComoLidas(chatId, usuarioId);

        return mensagemGateway.buscarMensagensPorChat(chatId);
    }
}
