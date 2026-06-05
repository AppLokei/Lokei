package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.exceptions.NotFoundException;
import Lokei.aplication.domain.exceptions.UsuarioNaoAutorizadoException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReativarAnuncioUseCase {
    private final AnuncioGateway anuncioGateway;
    private final UsuarioGateway usuarioGateway;

    public void execute(Long anuncioId, Long usuarioId) throws NotFoundException {
        Anuncio existente = anuncioGateway.buscarAnuncioPorId(anuncioId).orElseThrow(() -> new NotFoundException(anuncioId));
        var usuarioExiste = usuarioGateway.buscarUsuarioPorId(usuarioId);

        if (usuarioExiste.isEmpty()) {
            throw new NotFoundException(usuarioId);
        }

        if (!existente.getUsuarioId().equals(usuarioId)) {
            throw new UsuarioNaoAutorizadoException("Somente o proprietário do anúncio pode realizar esta operação");
        }

        existente.reativarAnuncio();

        anuncioGateway.atualizarAnuncio(existente);
    }
}
