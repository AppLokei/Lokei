package Lokei.aplication.infrastructure.persistence.mapper;

import Lokei.aplication.domain.entities.Usuario;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;

public class UsuarioMapper {
    public static Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Usuario(
                entity.getId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getCpf(),
                entity.getTelefone(),
                entity.getSenha()
        );
    }
}
