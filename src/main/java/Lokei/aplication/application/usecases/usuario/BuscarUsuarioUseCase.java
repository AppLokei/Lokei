package Lokei.aplication.application.usecases.usuario;

import Lokei.aplication.adapter.dto.res.PerfilResponse;
import Lokei.aplication.adapter.dto.support.UsuarioMapperSupport;
import Lokei.aplication.domain.exceptions.UsuarioNotFoundException;
import Lokei.aplication.infrastructure.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuscarUsuarioUseCase {

    @Autowired
    private UsuarioRepository usuarioRepo;

    public PerfilResponse buscarPorId(Long id) {
        return usuarioRepo.findById(id)
                .map(UsuarioMapperSupport::toPerfilResponse)
                .orElseThrow(() -> new UsuarioNotFoundException(id));
    }
}
