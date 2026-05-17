package Lokei.aplication.adapters.mapper;

import Lokei.aplication.adapters.dtos.req.AnuncioRequestDTO;
import Lokei.aplication.adapters.dtos.res.AnuncioResponseDTO;
import Lokei.aplication.adapters.dtos.res.FerramentaResponse;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Ferramenta;
import Lokei.aplication.infrastructure.persistence.entity.FerramentaEntity;
import Lokei.aplication.infrastructure.persistence.mapper.FerramentaMapper;

public class AnuncioControllerMapper {
    public static Anuncio toAnuncio(AnuncioRequestDTO dto) {
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
                anuncio.getFerramenta().id(),
                anuncio.getFerramenta().nome(),
                anuncio.getFerramenta().categoria()
        );

        return new AnuncioResponseDTO(
                anuncio.getId(),
                ferramenta,
                anuncio.getDescricao(),
                anuncio.getValorDiario(),
                anuncio.getDataCriacao(),
                anuncio.getStatus()
        );
    }
}
