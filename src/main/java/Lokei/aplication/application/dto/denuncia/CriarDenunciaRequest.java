package Lokei.aplication.application.dto.denuncia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CriarDenunciaRequest(
        @NotBlank(message = "Motivo da denuncia e obrigatorio.")
        String motivo,
        @NotBlank(message = "Descricao da denuncia e obrigatoria.")
        String descricao,
        List<String> imagens
) {
}
