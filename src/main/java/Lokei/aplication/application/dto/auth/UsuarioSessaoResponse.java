package Lokei.aplication.application.dto.auth;

public record UsuarioSessaoResponse(
        Integer id,
        String nome,
        String email,
        String papel
) {
}
