package Lokei.aplication.adapters.mapper;

import Lokei.aplication.adapters.dtos.req.AtualizarAnuncioRequest;
import Lokei.aplication.adapters.dtos.req.CriarAnuncioRequest;
import Lokei.aplication.adapters.dtos.res.AnuncioResponseDTO;
import Lokei.aplication.adapters.dtos.res.FerramentaResponse;
import Lokei.aplication.adapters.dtos.res.ImagemResponseDTO;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.domain.entities.Imagem;

import java.util.ArrayList;
import java.util.List;

public class AnuncioControllerMapper {
    public static Anuncio toAnuncio(CriarAnuncioRequest dto) {
        Ferramenta ferramenta = new Ferramenta(
                null,
                dto.ferramenta().nome(),
                dto.ferramenta().categoria()
        );

        return new Anuncio(
                null,
                dto.descricao(),
                dto.valorDiario(),
                ferramenta
        );
    }

    public static Anuncio toAnuncioUpdate(AtualizarAnuncioRequest dto) {
        Ferramenta ferramenta = new Ferramenta(
                null,
                dto.ferramenta().nome(),
                dto.ferramenta().categoria()
        );

        return new Anuncio(
                null,
                dto.descricao(),
                dto.valorDiario(),
                dto.status(),
                ferramenta
        );
    }

    public static AnuncioResponseDTO toResponseDTO(Anuncio anuncio) {
        FerramentaResponse ferramenta = new FerramentaResponse(
                anuncio.getFerramenta().getId(),
                anuncio.getFerramenta().getNome(),
                anuncio.getFerramenta().getCategoria()
        );

        List<ImagemResponseDTO> imagens = new ArrayList<>();
        for (Imagem imagem : anuncio.getImagens()) {
            imagens.add(new ImagemResponseDTO(imagem.getId(), imagem.getImagemUrl()));
        }

        return new AnuncioResponseDTO(
                anuncio.getId(),
                ferramenta,
                anuncio.getDescricao(),
                anuncio.getValorDiario(),
                anuncio.getDataCriacao(),
                anuncio.getStatus(),
                imagens
        );
    }
}
