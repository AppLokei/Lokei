package Lokei.aplication.adapter.dto.res;

import Lokei.aplication.domain.enums.MotivoDenunciaEnum;
import Lokei.aplication.domain.enums.StatusDenunciaEnum;

import java.time.LocalDateTime;

/**
 * DTO de resposta para Denúncia.
 */
public record DenunciaResponse(
        Long id,
        MotivoDenunciaEnum motivo,
        String descricao,
        LocalDateTime dataDenuncia,
        StatusDenunciaEnum status,
        Long anuncioId,
        Long denuncianteId
) {}
