package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Mensagem;

import java.util.List;
import java.util.Optional;

/**
 * Interface de porta de saída para persistência de Mensagens.
 */
public interface MensagemGateway {
    Mensagem enviarMensagem(Mensagem mensagem);
    Optional<Mensagem> buscarMensagemPorId(Long id);
    List<Mensagem> buscarMensagensPorChat(Long chatId);
    void marcarMensagensComoLidas(Long chatId, Long destinatarioId);
}
