package Lokei.aplication.adapters.mapper;

import Lokei.aplication.adapters.dtos.req.AnuncioRequestDTO;
import Lokei.aplication.adapters.dtos.res.AnuncioResponseDTO;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Ferramenta;
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

        return new AnuncioResponseDTO(
                anuncio.getId(),
                anuncio.getFerramenta().nome(),
                anuncio.getFerramenta().categoria(),
                anuncio.getDescricao(),
                anuncio.getValorDiario(),
                anuncio.getDataCriacao(),
                anuncio.getStatus()
        );
    }
}
