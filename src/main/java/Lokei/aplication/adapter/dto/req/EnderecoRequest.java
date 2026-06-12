package Lokei.aplication.adapter.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record EnderecoRequest(

        @NotBlank(message = "Logradouro e obrigatorio.")
        String logradouro,

        @NotBlank(message = "Bairro e obrigatorio.")
        String bairro,

        @NotBlank(message = "Numero e obrigatorio.")
        Integer numero,


        @NotBlank(message = "Cidade e obrigatoria.")
        String cidade,

        @NotBlank(message = "Estado e obrigatorio.")
        @Size(min = 2, max = 2, message = "Estado deve possuir 2 caracteres.")
        String estado,

        @NotBlank(message = "CEP e obrigatorio.")
        @Pattern(
                regexp = "\\d{8}",
                message = "CEP deve conter 8 digitos."
        )
        String cep

) {
}