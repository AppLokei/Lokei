package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.exceptions.NotFoundException;
import Lokei.aplication.domain.exceptions.UsuarioNaoAutorizadoException;
import Lokei.aplication.domain.gateways.AluguelGateway;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AtualizarAnuncioUseCase {

    private final AnuncioGateway anuncioGateway;
    private final AluguelGateway aluguelGateway;
    private final UsuarioGateway usuarioGateway;

    public Anuncio execute(Anuncio anuncio) {
        Anuncio existente = anuncioGateway.buscarAnuncioPorId(anuncio.getId()).orElseThrow(() -> new NotFoundException(anuncio.getId()));

        var usuarioExiste = usuarioGateway.buscarUsuarioPorId(anuncio.getUsuarioId());

        if (usuarioExiste.isEmpty()) {
            throw new NotFoundException(anuncio.getUsuarioId());
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
