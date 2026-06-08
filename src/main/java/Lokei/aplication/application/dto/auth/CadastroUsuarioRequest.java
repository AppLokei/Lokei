package Lokei.aplication.application.dto.auth;

import Lokei.aplication.application.dto.profile.EnderecoRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CadastroUsuarioRequest(
        @NotBlank(message = "Nome completo e obrigatorio.")
        String nome,
        @NotBlank(message = "E-mail e obrigatorio.")
        @Email(message = "Informe um e-mail valido.")
        String email,
        @NotBlank(message = "CPF e obrigatorio.")
        @Pattern(regexp = "\\d{11}", message = "Informe um CPF valido.")
        String cpf,
        @NotBlank(message = "Telefone e obrigatorio.")
        @Pattern(regexp = "\\d{10,11}", message = "Informe um telefone valido.")
        String telefone,
        @NotBlank(message = "Senha e obrigatoria.")
        String senha,
        @NotNull(message = "Aceite dos termos e obrigatorio.")
        Boolean aceitouTermos,
        String papel,
        @Valid
        EnderecoRequest endereco
) {
}
