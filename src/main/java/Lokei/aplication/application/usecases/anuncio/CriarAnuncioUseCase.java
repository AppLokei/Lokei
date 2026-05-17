package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.FerramentaGateway;

public class CriarAnuncioUseCase {

    private final AnuncioGateway anuncioGateway;
    private final FerramentaGateway ferramentaGateway;

    public CriarAnuncioUseCase(AnuncioGateway anuncioGateway, FerramentaGateway ferramentaGateway) {
        this.anuncioGateway = anuncioGateway;
        this.ferramentaGateway = ferramentaGateway;
    }

    public Anuncio execute(Anuncio anuncio) {

        Ferramenta ferramenta = ferramentaGateway.criarFerramenta(anuncio.getFerramenta());

        anuncio.setFerramenta(ferramenta);

        return anuncioGateway.criarAnuncio(anuncio);
    }

}
