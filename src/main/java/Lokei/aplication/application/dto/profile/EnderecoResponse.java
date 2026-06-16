package Lokei.aplication.application.dto.profile;

public record EnderecoResponse(
        String logradouro,
        String bairro,
        String numero,
        String complemento,
        String cidade,
        String estado,
        String cep
) {
}
