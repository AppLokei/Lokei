package Lokei.aplication.application.usecases.chat;

import Lokei.aplication.domain.entities.Chat;
import Lokei.aplication.domain.entities.Mensagem;
import Lokei.aplication.domain.exceptions.ChatNotFoundException;
import Lokei.aplication.domain.exceptions.UsuarioNaoAutorizadoException;
import Lokei.aplication.domain.gateways.ChatGateway;
import Lokei.aplication.domain.gateways.MensagemGateway;

/**
 * Caso de uso: Enviar uma mensagem em um chat existente.
 * RN-011: Apenas locador e locatário do chat podem enviar mensagens.
 */
public class EnviarMensagemUseCase {

    private final MensagemGateway mensagemGateway;
    private final ChatGateway chatGateway;

    public EnviarMensagemUseCase(MensagemGateway mensagemGateway, ChatGateway chatGateway) {
        this.mensagemGateway = mensagemGateway;
        this.chatGateway = chatGateway;
    }

    /**
     * @param chatId      ID do chat destino
     * @param remetenteId ID do usuário que envia
     * @param conteudo    Texto da mensagem
     * @return            Mensagem persistida
     */
    public Mensagem execute(Long chatId, Long remetenteId, String conteudo) {

        // Verifica existência do chat
        Chat chat = chatGateway.buscarChatPorId(chatId)
                .orElseThrow(() -> new ChatNotFoundException(chatId));

        // RN-011: Valida autorização via regra de negócio da entidade Chat
        if (!chat.usuarioPodeAcessar(remetenteId)) {
            throw new UsuarioNaoAutorizadoException(
                    "Apenas as partes envolvidas no anúncio podem enviar mensagens neste chat.");
        }

        Mensagem mensagem = new Mensagem(null, conteudo, null, false, remetenteId, chatId);
        return mensagemGateway.enviarMensagem(mensagem);
    }
}
