package Lokei.aplication.application.usecases.imagem;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.exceptions.UsuarioNaoAutorizadoException;
import Lokei.aplication.domain.exceptions.UsuarioNotFoundException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.ImagemGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;

public class AdicionarImagemUseCase {

    private final ImagemGateway imagemGateway;
    private final UsuarioGateway usuarioGateway;
    private final AnuncioGateway anuncioGateway;

    public AdicionarImagemUseCase(ImagemGateway imagemGateway, UsuarioGateway usuarioGateway, AnuncioGateway anuncioGateway) {
        this.imagemGateway = imagemGateway;
        this.usuarioGateway = usuarioGateway;
        this.anuncioGateway = anuncioGateway;
    }

    public Imagem execute(Imagem imagem, Long anuncioId, Long usuarioId){
        Anuncio existente = anuncioGateway.buscarAnuncioPorId(anuncioId).orElseThrow(() -> new AnuncioNotFoundException(anuncioId));
        var usuarioExiste = usuarioGateway.buscarUsuarioPorId(usuarioId);

        if (usuarioExiste.isEmpty()) {
            throw new UsuarioNotFoundException(usuarioId);
        }

        if (!existente.getUsuarioId().equals(usuarioId)) {
            throw new UsuarioNaoAutorizadoException("Somente o proprietário do anúncio pode realizar esta operação");
        }

        existente.adicionarImagem(imagem);

        return imagemGateway.salvarImagem(imagem, anuncioId);
    }
}
