package Lokei.aplication.adapter.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO de entrada para envio de mensagem em um chat.
 */
public record MensagemRequest(

        @NotBlank(message = "O conteúdo da mensagem não pode ser vazio.")
        String conteudo,

        /**
         * ID do remetente.
         * TODO: Substituir por extração do token de autenticação quando implementado.
         */
        @NotNull(message = "O ID do remetente é obrigatório.")
        Long remetenteId
) {}
