package Lokei.aplication.adapter.dto.res;

import java.time.LocalDateTime;

/**
 * DTO de resposta para Chat.
 */
public record ChatResponse(
        Long id,
        LocalDateTime dataCriacao,
        Long locatarioId,
        Long locadorId,
        Long anuncioId
) {}
