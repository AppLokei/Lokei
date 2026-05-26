package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.exceptions.UsuarioNotFoundException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;


public class CriarAnuncioUseCase {

    private final AnuncioGateway anuncioGateway;
    private final UsuarioGateway usuarioGateway;

    public CriarAnuncioUseCase(AnuncioGateway anuncioGateway, UsuarioGateway usuarioGateway) {
        this.anuncioGateway = anuncioGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public Anuncio execute(Anuncio anuncio) {
        var usuarioExiste = usuarioGateway.buscarUsuarioPorId(anuncio.getUsuarioId());

        if (usuarioExiste.isEmpty()) {
            throw new UsuarioNotFoundException(anuncio.getUsuarioId());
        }

        return anuncioGateway.criarAnuncio(anuncio);
    }

}
