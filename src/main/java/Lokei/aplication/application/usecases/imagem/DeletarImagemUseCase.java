package Lokei.aplication.application.usecases.imagem;

import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.exceptions.*;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.domain.gateways.ImagemGateway;
import Lokei.aplication.domain.gateways.UsuarioGateway;

import java.util.List;

public class DeletarImagemUseCase {
    private final ImagemGateway imagemGateway;
    private final UsuarioGateway usuarioGateway;
    private final AnuncioGateway anuncioGateway;

    public DeletarImagemUseCase(ImagemGateway imagemGateway, UsuarioGateway usuarioGateway, AnuncioGateway anuncioGateway) {
        this.imagemGateway = imagemGateway;
        this.usuarioGateway = usuarioGateway;
        this.anuncioGateway = anuncioGateway;
    }

    public Imagem execute(Long id, Long anuncioId, Long usuarioId) {
        Anuncio existente = anuncioGateway.buscarAnuncioPorId(anuncioId).orElseThrow(() -> new AnuncioNotFoundException(anuncioId));
        var usuarioExiste = usuarioGateway.buscarUsuarioPorId(usuarioId);

        if (usuarioExiste.isEmpty()) {
            throw new UsuarioNotFoundException(usuarioId);
        }
        if (!existente.getUsuarioId().equals(usuarioId)) {
            throw new UsuarioNaoAutorizadoException("Somente o proprietário do anúncio pode realizar esta operação");
        }

        Imagem imagem = imagemGateway.buscarImagemPorId(id).orElseThrow(() -> new ImagemNotFoundException(id));
        existente.removerImagem(imagem);

        imagemGateway.apagarImagem(imagem.getId());
        return imagem;
    }
}
