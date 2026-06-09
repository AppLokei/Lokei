package Lokei.aplication.domain.gateways;

import Lokei.aplication.domain.entities.Usuario;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;

import java.util.Optional;

public interface UsuarioGateway {
    Optional<Usuario> buscarUsuarioPorId(long id);
    Optional<UsuarioEntity> buscarUsuarioPorEmail(String id);
    Optional<UsuarioEntity> buscarUsuarioPorCpf(String id);
}
