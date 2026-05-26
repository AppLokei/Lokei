package Lokei.aplication.adapter.controllers;

import Lokei.aplication.adapter.dto.req.AnuncioFiltroRequest;
import Lokei.aplication.domain.entities.Imagem;
import Lokei.aplication.domain.exceptions.AnuncioNotFoundException;
import Lokei.aplication.domain.gateways.AnuncioGateway;
import Lokei.aplication.adapter.dto.req.AnuncioRequest;
import Lokei.aplication.adapter.dto.res.AnuncioResponse;
import Lokei.aplication.adapter.mapper.AnuncioControllerMapper;
import Lokei.aplication.application.usecases.anuncio.*;
import Lokei.aplication.domain.entities.Anuncio;
import Lokei.aplication.infrastructure.config.CloudinaryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class AnuncioController {

    private final CriarAnuncioUseCase criarAnuncioUseCase;
    private final AtualizarAnuncioUseCase atualizarAnuncioUseCase;
    private final DesativarAnuncioUseCase desativarAnuncioUseCase;
    private final PausarAnuncioUseCase pausarAnuncioUseCase;
    private final ReativarAnuncioUseCase reativarAnuncioUseCase;
    private final BuscarAnunciosUseCase buscarAnunciosUseCase;
    private final CloudinaryService cloudinaryService;
    private final AnuncioGateway anuncioGateway;

    public AnuncioController(CriarAnuncioUseCase criarAnuncioUseCase, AtualizarAnuncioUseCase atualizarAnuncioUseCase, DesativarAnuncioUseCase desativarAnuncioUseCase, PausarAnuncioUseCase pausarAnuncioUseCase, ReativarAnuncioUseCase reativarAnuncioUseCase, BuscarAnunciosUseCase buscarAnunciosUseCase, CloudinaryService cloudinaryService, AnuncioGateway anuncioGateway) {
        this.criarAnuncioUseCase = criarAnuncioUseCase;
        this.atualizarAnuncioUseCase = atualizarAnuncioUseCase;
        this.desativarAnuncioUseCase = desativarAnuncioUseCase;
        this.pausarAnuncioUseCase = pausarAnuncioUseCase;
        this.reativarAnuncioUseCase = reativarAnuncioUseCase;
        this.buscarAnunciosUseCase = buscarAnunciosUseCase;
        this.cloudinaryService = cloudinaryService;
        this.anuncioGateway = anuncioGateway;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/anuncio")
    public ResponseEntity<AnuncioResponse> criar(
            @RequestPart("anuncio") @Valid AnuncioRequest request,
            @RequestPart("imagens") List<MultipartFile> arquivos) {

        List<String> imagensUrl = arquivos.stream().map(arquivo -> cloudinaryService.uploadImagem(arquivo).get("url")).toList();

        List<Imagem> imagens = new ArrayList<>();
        for (MultipartFile arquivo : arquivos) {
            Map<String, String> resultado = cloudinaryService.uploadImagem(arquivo);
            imagens.add(new Imagem(null, resultado.get("url"), resultado.get("publicId")));
        }

        Long usuarioId = 1L; // substituir depois pelo metodo de autenticação
        Anuncio anuncio = AnuncioControllerMapper.toAnuncio(request, imagens, usuarioId, null);

        Anuncio criado = criarAnuncioUseCase.execute(anuncio);

        AnuncioResponse response = AnuncioControllerMapper.toResponse(criado);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/anuncio/{id}")
    public ResponseEntity<AnuncioResponse> atualizar(@PathVariable Long id, @RequestBody @Valid AnuncioRequest request) {

        Anuncio existente = anuncioGateway.buscarAnuncioPorId(id).orElseThrow(() -> new AnuncioNotFoundException(id));

        Long usuarioId = 1L; // substituir depois pelo metodo de autenticação
        Anuncio anuncio = AnuncioControllerMapper.toAnuncio(request, existente.getImagens(), usuarioId, id);

        Anuncio atualizado = atualizarAnuncioUseCase.execute(anuncio);

        AnuncioResponse response = AnuncioControllerMapper.toResponse(atualizado);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/anuncio/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        Long usuarioId = 1L; // substituir depois pelo metodo de autenticação
        desativarAnuncioUseCase.execute(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/anuncio/{id}/pausar")
    public ResponseEntity<Void> pausar(@PathVariable Long id) {

        Long usuarioId = 1L; // substituir depois pelo metodo de autenticação
        pausarAnuncioUseCase.execute(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/anuncio/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        Long usuarioId = 1L; // substituir depois pelo metodo de autenticação
        reativarAnuncioUseCase.execute(id, usuarioId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/anuncios")
    public ResponseEntity<Page<AnuncioResponse>> buscar(
            AnuncioFiltroRequest filtro,
            @RequestParam(defaultValue = "0") int pagina,
            @RequestParam(defaultValue = "12") int tamanho) {

        Page<Anuncio> anuncios = buscarAnunciosUseCase.execute(filtro, pagina, tamanho);

        return ResponseEntity.ok(anuncios.map(AnuncioControllerMapper::toResponse));
    }

}
