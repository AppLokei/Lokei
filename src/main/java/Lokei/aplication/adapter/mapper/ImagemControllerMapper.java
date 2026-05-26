package Lokei.aplication.adapter.mapper;

import Lokei.aplication.adapter.dto.res.ImagemResponse;
import Lokei.aplication.domain.entities.Imagem;

public class ImagemControllerMapper {
    public static ImagemResponse toResponseDTO(Imagem imagem) {
        return new ImagemResponse(
                imagem.getId(),
                imagem.getImagemUrl()
        );
    }
}
