package Lokei.aplication.application.dto.profile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AtualizarPerfilRequest(
        @NotBlank(message = "Nome completo e obrigatorio.")
        String nome,
        @NotBlank(message = "Telefone e obrigatorio.")
        @Pattern(regexp = "\\d{10,11}", message = "Informe um telefone valido.")
        String telefone,
        @Valid
        EnderecoRequest endereco
) {
}
