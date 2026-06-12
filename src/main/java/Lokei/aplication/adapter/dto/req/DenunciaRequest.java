package Lokei.aplication.adapter.dto.req;

import Lokei.aplication.domain.enums.MotivoDenunciaEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada para criação de denúncia.
 */
public record DenunciaRequest(

        @NotNull(message = "O motivo da denúncia é obrigatório.")
        MotivoDenunciaEnum motivo,

        @NotBlank(message = "A descrição da denúncia não pode ser vazia.")
        String descricao,

        /**
         * ID do usuário denunciante.
         * TODO: Substituir por extração do token de autenticação quando implementado.
         */
        @NotNull(message = "O ID do denunciante é obrigatório.")
        Long denuncianteId
) {}
