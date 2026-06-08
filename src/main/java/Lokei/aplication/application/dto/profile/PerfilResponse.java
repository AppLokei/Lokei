package Lokei.aplication.application.dto.profile;

public record PerfilResponse(
        Integer id,
        String nome,
        String email,
        String cpf,
        String telefone,
        String papel,
        EnderecoResponse endereco
) {
}
