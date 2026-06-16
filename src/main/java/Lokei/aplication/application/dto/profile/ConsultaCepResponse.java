package Lokei.aplication.application.dto.profile;

public record ConsultaCepResponse(
        String cep,
        String logradouro,
        String bairro,
        String cidade,
        String estado
) {
}
