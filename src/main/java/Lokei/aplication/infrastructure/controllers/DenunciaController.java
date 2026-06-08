package Lokei.aplication.infrastructure.controllers;

import Lokei.aplication.application.dto.denuncia.CriarDenunciaRequest;
import Lokei.aplication.application.dto.denuncia.DenunciaResponse;
import Lokei.aplication.application.service.DenunciaService;
import Lokei.aplication.infrastructure.security.UsuarioAutenticadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DenunciaController {

    private final DenunciaService denunciaService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public DenunciaController(DenunciaService denunciaService, UsuarioAutenticadoService usuarioAutenticadoService) {
        this.denunciaService = denunciaService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @PostMapping("/anuncios/{id}/denuncias")
    public ResponseEntity<DenunciaResponse> denunciar(@PathVariable Integer id, @Valid @RequestBody CriarDenunciaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(denunciaService.criar(usuarioAutenticadoService.getUsuarioId(), id, request));
    }
}
