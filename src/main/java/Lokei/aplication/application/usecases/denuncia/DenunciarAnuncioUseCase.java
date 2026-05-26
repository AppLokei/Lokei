package Lokei.aplication.application.usecases.denuncia;

import Lokei.aplication.domain.entities.Denuncia;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.exceptions.UsuarioNotFoundException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.DenunciaGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;

public class DenunciarAnuncioUseCase {
    private final DenunciaGateway denunciaGateway;
    private final AnuncioGateway anuncioGateway;
    private final UsuarioGateway usuarioGateway;

    public DenunciarAnuncioUseCase(DenunciaGateway denunciaGateway, AnuncioGateway anuncioGateway, UsuarioGateway usuarioGateway) {
        this.denunciaGateway = denunciaGateway;
        this.anuncioGateway = anuncioGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public Denuncia execute(Denuncia denuncia, Long anuncioId, Long usuarioId) {
        var anuncioExiste = anuncioGateway.buscarAnuncioPorId(anuncioId);
        var usuarioExiste = usuarioGateway.buscarUsuarioPorId(usuarioId);

        if (anuncioExiste.isEmpty()) {
            throw new AnuncioNotFoundException(anuncioId);
        }

        if (usuarioExiste.isEmpty()) {
            throw new UsuarioNotFoundException(usuarioId);
        }

        // verificar se usuario está autenticado antes de realizar denuncia

        Denuncia criado = new Denuncia(
                null,
                denuncia.getMotivo(),
                denuncia.getDescricao(),
                usuarioId,
                anuncioId
        );

        return denunciaGateway.criarDenuncia(criado);
    }
}
