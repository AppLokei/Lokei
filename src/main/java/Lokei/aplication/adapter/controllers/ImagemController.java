package Lokei.aplication.adapter.controllers;

import Lokei.aplication.adapter.dto.res.ImagemResponse;
import Lokei.aplication.adapter.mapper.ImagemControllerMapper;
import Lokei.aplication.application.usecases.imagem.AdicionarImagemUseCase;
import Lokei.aplication.application.usecases.imagem.DeletarImagemUseCase;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.infrastructure.config.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ImagemController {

    private final AdicionarImagemUseCase adicionarImagemUseCase;
    private final DeletarImagemUseCase deletarImagemUseCase;
    private final CloudinaryService cloudinaryService;

    @PostMapping(value = "/anuncio/{id}/imagem")
    public ResponseEntity<ImagemResponse> salvar(@PathVariable Long id, @RequestParam("file") MultipartFile arquivo) {

        Imagem.validaImagem(arquivo.getContentType(), arquivo.getSize());
        Long usuarioId = 1L; // substituir depois pelo metodo de autenticação
        Map<String, String> dadosImagem = cloudinaryService.uploadImagem(arquivo);
        Imagem imagem = new Imagem(null, dadosImagem.get("url"), dadosImagem.get("publicId"));
        Imagem salvo = adicionarImagemUseCase.execute(imagem, id, usuarioId);

        ImagemResponse response = ImagemControllerMapper.toResponseDTO(salvo);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/anuncio/{anuncioId}/imagens/{imagemId}")
    public ResponseEntity<Void> apagar(@PathVariable Long anuncioId, @PathVariable Long imagemId) {

        Long usuarioId = 1L; // substituir depois pelo metodo de autenticação
        Imagem imagem = deletarImagemUseCase.execute(imagemId, anuncioId, usuarioId);
        cloudinaryService.deletarImagem(imagem.getPublicId());
        return ResponseEntity.noContent().build();
    }
}
