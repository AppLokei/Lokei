package Lokei.aplication.domain.gateway;

import Lokei.aplication.domain.entities.Imagem;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImagemGateway {
    Imagem salvarImagem(Imagem imagem, Long anuncioId);
    List<Imagem> buscarImagensPorAnuncio(Long anuncioId);
    void apagarImagem(Long id);
}
