package Lokei.aplication.adapter.mapper;

import Lokei.aplication.adapter.dto.req.AnuncioRequest;
import Lokei.aplication.adapter.dto.res.AnuncioResponse;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.domain.entities.Imagem;

import java.util.ArrayList;
import java.util.List;

public class AnuncioControllerMapper {
    public static Anuncio toAnuncio(AnuncioRequest request, List<Imagem> imagens, Long usuarioId, Long anuncioId) {
        Ferramenta ferramenta = new Ferramenta(
                null,
                request.ferramenta().nome(),
                request.ferramenta().categoria()
        );

        return new Anuncio(
                anuncioId,
                request.titulo(),
                request.descricao(),
                request.valorDiario(),
                null,
                ferramenta,
                imagens,
                usuarioId
        );
    }

    public static AnuncioResponse toResponse(Anuncio anuncio) {

        List<String> imagensUrl = new ArrayList<>();
        for (Imagem imagem : anuncio.getImagens()) {
            imagensUrl.add(imagem.getImagemUrl());
        }

        return new AnuncioResponse(
                anuncio.getId(),
                anuncio.getTitulo(),
                anuncio.getDescricao(),
                anuncio.getValorDiario(),
                anuncio.getDataCriacao(),
                anuncio.getStatus(),
                imagensUrl
        );
    }
}
