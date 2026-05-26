package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.Usuario;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import Lokei.aplication.infrastructure.persistence.entities.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;
import Lokei.aplication.infrastructure.persistence.mapper.AnuncioMapper;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsuarioRepositoryGateway implements UsuarioGateway {

    private final UsuarioRepository usuarioRepository;

    public UsuarioRepositoryGateway(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Optional<UsuarioEntity> buscarUsuarioPorId(long id) {
        return usuarioRepository.findById(id);
    }
}
