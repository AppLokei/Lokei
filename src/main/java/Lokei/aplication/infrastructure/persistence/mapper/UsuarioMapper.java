package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Usuario;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;

public class UsuarioMapper {
    public static Usuario toDomain(UsuarioEntity entity) {
        return new Usuario();
    }
}
