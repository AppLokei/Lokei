package Lokei.aplication.adapters.controllers;

import Lokei.aplication.adapters.dtos.res.ImagemResponseDTO;
import Lokei.aplication.adapters.mapper.ImagemControllerMapper;
import Lokei.aplication.application.usecases.anuncio.BuscarAnuncioPorIdUseCase;
import Lokei.aplication.application.usecases.imagem.DeletarImagemPorAnuncioUseCase;
import Lokei.aplication.application.usecases.imagem.BuscarImagensPorAnuncioUseCase;
import Lokei.aplication.application.usecases.imagem.SalvarImagemUseCase;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.infrastructure.config.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ImagemController {

    private final SalvarImagemUseCase salvarImagemUseCase;
    private final DeletarImagemPorAnuncioUseCase apagarImagemUseCase;
    private final BuscarImagensPorAnuncioUseCase buscarImagensPorAnuncioUseCase;
    private final BuscarAnuncioPorIdUseCase buscarAnuncioPorIdUseCase;
    private final CloudinaryService cloudinaryService;

    public ImagemController(SalvarImagemUseCase salvarImagemUseCase, DeletarImagemPorAnuncioUseCase apagarImagemUseCase, BuscarImagensPorAnuncioUseCase buscarImagensPorAnuncioUseCase, BuscarAnuncioPorIdUseCase buscarAnuncioPorIdUseCase, CloudinaryService cloudinaryService) {
        this.salvarImagemUseCase = salvarImagemUseCase;
        this.apagarImagemUseCase = apagarImagemUseCase;
        this.buscarImagensPorAnuncioUseCase = buscarImagensPorAnuncioUseCase;
        this.buscarAnuncioPorIdUseCase = buscarAnuncioPorIdUseCase;
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping(value = "/anuncio/{id}/imagem")
    public ResponseEntity<ImagemResponseDTO> salvar(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        Anuncio anuncio = buscarAnuncioPorIdUseCase.execute(id);
        String imagemUrl = cloudinaryService.uploadImagem(file);

        Imagem imagem = new Imagem(null, imagemUrl, anuncio);
        Imagem salvo = salvarImagemUseCase.execute(imagem, id);

        ImagemResponseDTO response = ImagemControllerMapper.toResponseDTO(salvo);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/imagem/{id}")
    public ResponseEntity<Void> apagar(@PathVariable Long id) {
        apagarImagemUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/anuncio/{id}/imagens")
    public ResponseEntity<List<ImagemResponseDTO>> buscarImagensDeAnuncio(@PathVariable Long id) {
        List<Imagem> imagens = buscarImagensPorAnuncioUseCase.execute(id);
        List<ImagemResponseDTO> response = new ArrayList<>();
        for (Imagem imagem : imagens) {
            response.add(ImagemControllerMapper.toResponseDTO(imagem));
        }

        return ResponseEntity.ok(response);
    }
}
