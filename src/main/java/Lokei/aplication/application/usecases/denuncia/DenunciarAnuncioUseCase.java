package Lokei.aplication.application.usecases.denuncia;

import Lokei.aplication.domain.entities.Denuncia;
import Lokei.aplication.domain.exceptions.NotFoundException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.DenunciaGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DenunciarAnuncioUseCase {
    private final DenunciaGateway denunciaGateway;
    private final AnuncioGateway anuncioGateway;
    private final UsuarioGateway usuarioGateway;

    public Denuncia execute(Denuncia denuncia, Long anuncioId, Long usuarioId) {
        var anuncioExiste = anuncioGateway.buscarAnuncioPorId(anuncioId);
        var usuarioExiste = usuarioGateway.buscarUsuarioPorId(usuarioId);

        if (anuncioExiste.isEmpty()) {
            throw new NotFoundException(anuncioId);
        }

        if (usuarioExiste.isEmpty()) {
            throw new NotFoundException(usuarioId);
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
