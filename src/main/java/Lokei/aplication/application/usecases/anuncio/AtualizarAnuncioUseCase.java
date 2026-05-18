package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.FerramentaGateway;

public class AtualizarAnuncioUseCase {

    private final AnuncioGateway anuncioGateway;
    private final FerramentaGateway ferramentaGateway;

    public AtualizarAnuncioUseCase(AnuncioGateway anuncioGateway, FerramentaGateway ferramentaGateway) {
        this.anuncioGateway = anuncioGateway;
        this.ferramentaGateway = ferramentaGateway;
    }

    public Anuncio execute(Long id, Anuncio anuncio) {
        Anuncio existente = anuncioGateway.buscarAnuncioPorId(id).orElseThrow(() -> new AnuncioNotFoundException(id));
        Ferramenta ferramenta = new Ferramenta(
          existente.getFerramenta().getId(),
          anuncio.getFerramenta().getNome(),
          anuncio.getFerramenta().getCategoria()
        );

        anuncio.setFerramenta(ferramenta);

        Ferramenta ferramentaAtualizada = ferramentaGateway.atualizarFerramenta(ferramenta);
        anuncio.setFerramenta(ferramentaAtualizada);

        return anuncioGateway.atualizarAnuncio(anuncio);
    }
}
