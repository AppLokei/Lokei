package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.gateways.UsuarioGateway;
import Lokei.aplication.infrastructure.persistence.entities.UsuarioEntity;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UsuarioRepositoryGateway implements UsuarioGateway {

    private final UsuarioRepository usuarioRepository;

    @Override
    public Optional<UsuarioEntity> buscarUsuarioPorId(long id) {
        return usuarioRepository.findById(id);
    }
}
