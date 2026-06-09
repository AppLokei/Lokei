package Lokei.aplication.application.usecases.chat;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Chat;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.exceptions.ChatInvalidoException;
import Lokei.aplication.domain.exceptions.UsuarioNotFoundException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.ChatGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;

/**
 * Caso de uso: Inicia ou recupera um chat existente entre locatário e locador.
 * HU-019: Locatário pode iniciar contato com o locador a partir de um anúncio.
 * RN-011: Somente as partes envolvidas podem acessar o chat.
 */
public class IniciarOuBuscarChatUseCase {

    private final ChatGateway chatGateway;
    private final AnuncioGateway anuncioGateway;
    private final UsuarioGateway usuarioGateway;

    public IniciarOuBuscarChatUseCase(ChatGateway chatGateway,
                                       AnuncioGateway anuncioGateway,
                                       UsuarioGateway usuarioGateway) {
        this.chatGateway = chatGateway;
        this.anuncioGateway = anuncioGateway;
        this.usuarioGateway = usuarioGateway;
    }

    /**
     * @param anuncioId   ID do anúncio de interesse
     * @param locatarioId ID do usuário que deseja contato (locatário)
     * @return            Chat existente ou recém-criado
     */
    public Chat execute(Long anuncioId, Long locatarioId) {

        // Valida existência do anúncio
        Anuncio anuncio = anuncioGateway.buscarAnuncioPorId(anuncioId)
                .orElseThrow(() -> new AnuncioNotFoundException(anuncioId));

        // Apenas anúncios ATIVOS podem ser contatados
        if (!StatusAnuncioEnum.ATIVO.equals(anuncio.getStatus())) {
            throw new ChatInvalidoException("Não é possível iniciar chat com um anúncio que não está ativo.");
        }

        Long locadorId = anuncio.getUsuarioId();

        // O locador não pode iniciar chat com si mesmo
        if (locatarioId.equals(locadorId)) {
            throw new ChatInvalidoException("O proprietário do anúncio não pode iniciar chat consigo mesmo.");
        }

        // Valida existência do locatário
        usuarioGateway.buscarUsuarioPorId(locatarioId)
                .orElseThrow(() -> new UsuarioNotFoundException(locatarioId));

        // Se já existe chat entre eles para este anúncio, retorna o existente (idempotência)
        return chatGateway.buscarChatPorAnuncioELocatario(anuncioId, locatarioId)
                .orElseGet(() -> {
                    Chat novoChat = new Chat(null, null, locatarioId, locadorId, anuncioId);
                    return chatGateway.criarChat(novoChat);
                });
    }
}
