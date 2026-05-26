package Lokei.aplication.application.usecases.anuncio;

import Lokei.aplication.adapter.dto.req.AnuncioFiltroRequest;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import org.springframework.data.domain.Page;

public class BuscarAnunciosUseCase {
    private final AnuncioGateway anuncioGateway;

    public BuscarAnunciosUseCase(AnuncioGateway anuncioGateway) {
        this.anuncioGateway = anuncioGateway;
    }

        public Page<Anuncio> execute(AnuncioFiltroRequest filtro, int pagina, int tamanho) {
        return anuncioGateway.buscarAnunciosComFiltro(
                filtro.titulo(),
                filtro.categoria(),
                filtro.valorMin(),
                filtro.valorMax(),
                pagina,
                tamanho
        );
    }
}
