package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.enums.StatusAnuncioEnum;
import Lokei.aplication.domain.exceptions.AnuncioInvalidoException;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.exceptions.UsuarioNaoAutorizadoException;
import Lokei.aplication.domain.exceptions.UsuarioNotFoundException;
import Lokei.aplication.domain.gateways.AluguelGateway;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;

public class PausarAnuncioUseCase {
    private final AnuncioGateway anuncioGateway;
    private final UsuarioGateway usuarioGateway;
    private final AluguelGateway aluguelGateway;

    public PausarAnuncioUseCase(AnuncioGateway anuncioGateway, UsuarioGateway usuarioGateway, AluguelGateway aluguelGateway) {
        this.anuncioGateway = anuncioGateway;
        this.usuarioGateway = usuarioGateway;
        this.aluguelGateway = aluguelGateway;
    }

    public void execute(Long anuncioId, Long usuarioId) {
        Anuncio existente = anuncioGateway.buscarAnuncioPorId(anuncioId).orElseThrow(() -> new AnuncioNotFoundException(anuncioId));
        var usuarioExiste = usuarioGateway.buscarUsuarioPorId(usuarioId);

        if (usuarioExiste.isEmpty()) {
            throw new UsuarioNotFoundException(usuarioId);
        }

        if (!existente.getUsuarioId().equals(usuarioId)) {
            throw new UsuarioNaoAutorizadoException("Somente o proprietário do anúncio pode realizar esta operação");
        }

        boolean aluguelEmAndamento = aluguelGateway.existeAluguelEmAndamentoPorAnuncio(anuncioId);

        if (existente.getStatus().equals(StatusAnuncioEnum.PAUSADO)) {
            throw new AnuncioInvalidoException("Nao é possivel pausar uma anuncio que ja esta pausado");
        }

        existente.pausarAnuncio(aluguelEmAndamento);

        anuncioGateway.atualizarAnuncio(existente);
    }
}
