package Lokei.aplication.infrastructure.services;

import Lokei.aplication.domain.services.NotificacaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Implementação mock do serviço de notificação.
 * No futuro, substituir por integração real (e-mail, push notification, etc.).
 */
@Service
public class NotificacaoServiceMock implements NotificacaoService {

    private static final Logger log = LoggerFactory.getLogger(NotificacaoServiceMock.class);

    @Override
    public void notificarAnuncioDesativadoPorDenuncia(Long locadorId, Long anuncioId) {
        // MOCK: apenas loga a notificação. Substituir por envio de e-mail ou push.
        log.warn("[NOTIFICAÇÃO MOCK] Locador id={} foi notificado: seu anúncio id={} foi DESATIVADO por aprovação de denúncia.",
                locadorId, anuncioId);
    }
}
