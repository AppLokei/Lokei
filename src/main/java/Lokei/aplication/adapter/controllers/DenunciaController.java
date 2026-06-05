package Lokei.aplication.adapter.controllers;

import Lokei.aplication.adapter.dto.req.DenunciaRequest;
import Lokei.aplication.adapter.dto.res.DenunciaResponse;
import Lokei.aplication.adapter.mapper.DenunciaControllerMapper;
import Lokei.aplication.application.usecases.denuncia.DenunciarAnuncioUseCase;
import Lokei.aplication.domain.entities.Denuncia;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/denuncia")
@RequiredArgsConstructor
public class DenunciaController {

    private final DenunciarAnuncioUseCase denunciarAnuncioUseCase;

    @PostMapping("/anuncio/{id}")
    public ResponseEntity<DenunciaResponse> criar(@PathVariable Long id, @RequestBody DenunciaRequest request){

        Long usuarioId = 1L; // substituir depois pelo metodo de autenticação
        Denuncia denuncia = DenunciaControllerMapper.toDenuncia(request, id, usuarioId);

        Denuncia criado = denunciarAnuncioUseCase.execute(denuncia, id, usuarioId);

        DenunciaResponse response = DenunciaControllerMapper.toResponse(criado);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
