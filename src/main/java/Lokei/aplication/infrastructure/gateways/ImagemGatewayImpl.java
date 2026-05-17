package Lokei.aplication.infrastructure.gateways;

import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.gateways.ImagemGateway;
import Lokei.aplication.infrastructure.persistence.entity.AnuncioEntity;
import Lokei.aplication.infrastructure.persistence.entity.ImagemEntity;
import Lokei.aplication.infrastructure.persistence.mapper.AnuncioMapper;
import Lokei.aplication.infrastructure.persistence.mapper.ImagemMapper;
import Lokei.aplication.infrastructure.persistence.repository.AnuncioEntityRepository;
import Lokei.aplication.infrastructure.persistence.repository.ImagemEntityRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImagemGatewayImpl implements ImagemGateway {

    private final ImagemEntityRepository imagemEntityRepository;
    private final AnuncioEntityRepository anuncioEntityRepository;

    public ImagemGatewayImpl(ImagemEntityRepository imagemEntityRepository, AnuncioEntityRepository anuncioEntityRepository) {
        this.imagemEntityRepository = imagemEntityRepository;
        this.anuncioEntityRepository = anuncioEntityRepository;
    }

    @Override
    public Imagem salvarImagem(Imagem imagem, Long anuncioId) {
        AnuncioEntity anuncio = anuncioEntityRepository.findById(anuncioId).orElseThrow(() -> new AnuncioNotFoundException(anuncioId));
        ImagemEntity entity = ImagemMapper.toEntity(imagem, anuncio);
        ImagemEntity salvo = imagemEntityRepository.save(entity);
        return ImagemMapper.toDomain(salvo, AnuncioMapper.toDomain(anuncio));
    }

    @Override
    public void apagarImagem(Long id) {
        imagemEntityRepository.deleteById(id);
    }

    @Override
    public List<Imagem> buscarImagensPorAnuncio(Long anuncioId) {
        AnuncioEntity anuncio = anuncioEntityRepository.findById(anuncioId).orElseThrow(() -> new AnuncioNotFoundException(anuncioId));

        List<ImagemEntity> entities = imagemEntityRepository.findImagemEntitiesByAnuncio(anuncio);
        List<Imagem> imagens = new ArrayList<>();
        for(ImagemEntity entity : entities) {
            imagens.add(ImagemMapper.toDomain(entity, AnuncioMapper.toDomain(anuncio)));
        }

        return imagens;
    }
}
