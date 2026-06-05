package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.exceptions.NotFoundException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CriarAnuncioUseCase {

    private final AnuncioGateway anuncioGateway;
    private final UsuarioGateway usuarioGateway;

    public Anuncio execute(Anuncio anuncio) {
        var usuarioExiste = usuarioGateway.buscarUsuarioPorId(anuncio.getUsuarioId());

        if (usuarioExiste.isEmpty()) {
            throw new NotFoundException(anuncio.getUsuarioId());
        }

        return anuncioGateway.criarAnuncio(anuncio);
    }

}
