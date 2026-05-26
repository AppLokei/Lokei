package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.exceptions.UsuarioNaoAutorizadoException;
import Lokei.aplication.domain.exceptions.UsuarioNotFoundException;
import Lokei.aplication.domain.gateways.AluguelGateway;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;

public class AtualizarAnuncioUseCase {

    private final AnuncioGateway anuncioGateway;
    private final AluguelGateway aluguelGateway;
    private final UsuarioGateway usuarioGateway;

    public AtualizarAnuncioUseCase(AnuncioGateway anuncioGateway, AluguelGateway aluguelGateway, UsuarioGateway usuarioGateway) {
        this.anuncioGateway = anuncioGateway;
        this.aluguelGateway = aluguelGateway;
        this.usuarioGateway = usuarioGateway;
    }

    public Anuncio execute(Anuncio anuncio) {
        Anuncio existente = anuncioGateway.buscarAnuncioPorId(anuncio.getId()).orElseThrow(() -> new AnuncioNotFoundException(anuncio.getId()));

        var usuarioExiste = usuarioGateway.buscarUsuarioPorId(anuncio.getUsuarioId());

        if (usuarioExiste.isEmpty()) {
            throw new UsuarioNotFoundException(anuncio.getUsuarioId());
        }

        if (!existente.getUsuarioId().equals(anuncio.getUsuarioId())) {
            throw new UsuarioNaoAutorizadoException("Somente o proprietário do anúncio pode realizar esta operação");
        }

        boolean aluguelEmAndamento = aluguelGateway.existeAluguelEmAndamentoPorAnuncio(anuncio.getId());

        existente.atualizarDados(
                anuncio.getTitulo(),
                anuncio.getDescricao(),
                anuncio.getValorDiario(),
                anuncio.getImagens(),
                anuncio.getFerramenta(),
                aluguelEmAndamento
        );

        return anuncioGateway.atualizarAnuncio(existente);
    }

}
