package Lokei.aplication.domain.gateways;

import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;

import java.util.Optional;

public interface UsuarioGateway {
    Optional<UsuarioEntity> buscarUsuarioPorId(long id);
}
