package Lokei.aplication.adapter.dto.res;

import java.time.LocalDateTime;

/**
 * DTO de resposta para Mensagem.
 */
public record MensagemResponse(
        Long id,
        String conteudo,
        LocalDateTime dataHoraEnvio,
        boolean lida,
        Long remetenteId,
        Long chatId
) {}
