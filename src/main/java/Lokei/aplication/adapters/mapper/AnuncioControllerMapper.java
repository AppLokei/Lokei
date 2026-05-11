package Lokei.aplication.adapters.mapper;

import Lokei.aplication.adapters.dtos.AnuncioRequestDTO;
import Lokei.aplication.adapters.dtos.AnuncioResponseDTO;
import Lokei.aplication.domain.entities.Anuncio;

public class AnuncioControllerMapper {
    public static Anuncio toAnuncio(AnuncioRequestDTO dto) {
        return new Anuncio(
                null,
                dto.titulo(),
                dto.descricao(),
                dto.valorDiario(),
                dto.status(),
                dto.categoria()
        );
    }

    public static AnuncioResponseDTO toResponseDTO(Anuncio anuncio) {
        return new AnuncioResponseDTO(
                anuncio.getId(),
                anuncio.getTitulo(),
                anuncio.getDescricao(),
                anuncio.getValorDiario(),
                anuncio.getDataCriacao(),
                anuncio.getStatus(),
                anuncio.getCategoria()
        );
    }
}
