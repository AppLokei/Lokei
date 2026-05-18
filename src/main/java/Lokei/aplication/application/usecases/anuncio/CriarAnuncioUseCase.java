package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.FerramentaGateway;
import Lokei.aplication.domain.gateways.ImagemGateway;

import java.util.List;

public class CriarAnuncioUseCase {

    private final AnuncioGateway anuncioGateway;
    private final FerramentaGateway ferramentaGateway;
    private final ImagemGateway imagemGateway;

    public CriarAnuncioUseCase(AnuncioGateway anuncioGateway, FerramentaGateway ferramentaGateway, ImagemGateway imagemGateway) {
        this.anuncioGateway = anuncioGateway;
        this.ferramentaGateway = ferramentaGateway;
        this.imagemGateway = imagemGateway;
    }

    public Anuncio execute(Anuncio anuncio, List<String> imagensUrls) {

        anuncio.validaImagens(imagensUrls);

        Ferramenta ferramenta = ferramentaGateway.criarFerramenta(anuncio.getFerramenta());

        anuncio.setFerramenta(ferramenta);

        Anuncio criado = anuncioGateway.criarAnuncio(anuncio);

        for (String url : imagensUrls) {
            imagemGateway.salvarImagem(new Imagem(null, url, criado), criado.getId());
        }

        return criado;
    }

}
