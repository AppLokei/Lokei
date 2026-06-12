package Lokei.aplication.adapter.dto.res;

public record AuthResponse(
        String token,
        String tipo,
        UsuarioSessaoResponse usuario
) {
}
