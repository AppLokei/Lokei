package Lokei.aplication.application.dto.auth;

public record AuthResponse(
        String token,
        String tipo,
        UsuarioSessaoResponse usuario
) {
}
