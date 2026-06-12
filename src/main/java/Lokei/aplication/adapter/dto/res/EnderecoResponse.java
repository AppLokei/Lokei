package Lokei.aplication.adapter.dto.res;

public record EnderecoResponse(
        String logradouro,
        String bairro,
        Integer numero,
        String cidade,
        String estado,
        String cep
) {
}
