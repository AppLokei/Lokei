package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.FerramentaGateway;
import Lokei.aplication.domain.gateways.ImagemGateway;

import java.util.List;

public class AtualizarAnuncioUseCase {

    private final AnuncioGateway anuncioGateway;
    private final FerramentaGateway ferramentaGateway;
    private final ImagemGateway imagemGateway;

    public AtualizarAnuncioUseCase(AnuncioGateway anuncioGateway, FerramentaGateway ferramentaGateway, ImagemGateway imagemGateway) {
        this.anuncioGateway = anuncioGateway;
        this.ferramentaGateway = ferramentaGateway;
        this.imagemGateway = imagemGateway;
    }

    public Anuncio execute(Long id, Anuncio anuncio, List<String> imagensUrl) {
        Anuncio existente = anuncioGateway.buscarAnuncioPorId(id).orElseThrow(() -> new AnuncioNotFoundException(id));

        Ferramenta ferramenta = new Ferramenta(
          existente.getFerramenta().getId(),
          anuncio.getFerramenta().getNome(),
          anuncio.getFerramenta().getCategoria()
        );

        anuncio.setFerramenta(ferramenta);
        Ferramenta ferramentaAtualizada = ferramentaGateway.atualizarFerramenta(ferramenta);
        anuncio.setFerramenta(ferramentaAtualizada);

        Anuncio anuncioAtualizado = anuncioGateway.atualizarAnuncio(anuncio);

        if (imagensUrl != null && !imagensUrl.isEmpty()) {
            anuncio.validaImagens(imagensUrl);
            imagemGateway.deletarImagemPorAnuncio(id);
            for (String url : imagensUrl) {
                imagemGateway.salvarImagem(new Imagem(null, url, anuncioAtualizado), id);
            }
        }

        return anuncioAtualizado;
    }
}
