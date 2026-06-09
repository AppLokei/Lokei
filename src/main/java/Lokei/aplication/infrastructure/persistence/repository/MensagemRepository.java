package Lokei.aplication.infrastructure.persistence.repository;

import Lokei.aplication.infrastructure.persistence.entities.MensagemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MensagemRepository extends JpaRepository<MensagemEntity, Long> {

    List<MensagemEntity> findByChatIdOrderByDataHoraEnvioAsc(Long chatId);

    /**
     * Marca como lidas todas as mensagens de um chat que NÃO foram enviadas pelo destinatário
     * (ou seja, mensagens do outro participante que ainda não foram lidas).
     */
    @Modifying
    @Transactional
    @Query("UPDATE MensagemEntity m SET m.lida = true WHERE m.chat.id = :chatId AND m.remetente.id <> :destinatarioId AND m.lida = false")
    void marcarComoLidas(@Param("chatId") Long chatId, @Param("destinatarioId") Long destinatarioId);
}
