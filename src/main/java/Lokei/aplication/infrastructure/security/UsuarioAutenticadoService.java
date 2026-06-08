package Lokei.aplication.infrastructure.security;

import Lokei.aplication.infrastructure.shared.exception.NaoAutenticadoException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAutenticadoService {

    public UsuarioAutenticado getUsuarioAutenticado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof UsuarioAutenticado usuario)) {
            throw new NaoAutenticadoException("Autenticacao obrigatoria.");
        }
        return usuario;
    }

    public Integer getUsuarioId() {
        return getUsuarioAutenticado().getId();
    }
}
