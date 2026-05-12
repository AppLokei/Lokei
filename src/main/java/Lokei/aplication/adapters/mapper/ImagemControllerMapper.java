package Lokei.aplication.adapters.mapper;

import Lokei.aplication.adapters.dtos.res.ImagemResponseDTO;
import Lokei.aplication.domain.entities.Imagem;

public class ImagemControllerMapper {
    public static ImagemResponseDTO toResponseDTO(Imagem imagem) {
        return new ImagemResponseDTO(
                imagem.getId(),
                imagem.getImagemUrl()
        );
    }
}
