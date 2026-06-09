package Lokei.aplication.domain.services;

/**
 * Contrato para serviço de notificação ao locador.
 * Implementação atual é um mock; pode ser substituída por e-mail, push, etc.
 */
public interface NotificacaoService {
    void notificarAnuncioDesativadoPorDenuncia(Long locadorId, Long anuncioId);
}
