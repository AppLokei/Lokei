package Lokei.aplication.application.dto.chat;

import jakarta.validation.constraints.NotBlank;

public record MensagemChatRequest(
        @NotBlank(message = "Conteudo da mensagem e obrigatorio.")
        String conteudo
) {
}
