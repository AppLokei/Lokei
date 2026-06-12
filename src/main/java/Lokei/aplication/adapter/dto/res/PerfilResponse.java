package Lokei.aplication.adapter.dto.res;

public record PerfilResponse(
        Long id,
        String nome,
        String email,
        String cpf,
        String telefone,
        EnderecoResponse endereco
) {

}
