package Lokei.aplication.adapter.mapper;

import Lokei.aplication.adapter.dto.res.ImagemResponseDTO;
import Lokei.aplication.domain.entities.Imagem;

public class ImagemControllerMapper {
    public static ImagemResponseDTO toResponseDTO(Imagem imagem) {
        return new ImagemResponseDTO(
                imagem.getId(),
                imagem.getImagemUrl()
        );
    }
}
