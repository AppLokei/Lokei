package Lokei.aplication.infrastructure.controllers;

import Lokei.aplication.application.dto.denuncia.DenunciaResponse;
import Lokei.aplication.application.dto.denuncia.ModerarDenunciaRequest;
import Lokei.aplication.application.service.DenunciaService;
import Lokei.aplication.infrastructure.security.UsuarioAutenticadoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/denuncias")
public class AdminDenunciaController {

    private final DenunciaService denunciaService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public AdminDenunciaController(DenunciaService denunciaService, UsuarioAutenticadoService usuarioAutenticadoService) {
        this.denunciaService = denunciaService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @GetMapping
    public ResponseEntity<List<DenunciaResponse>> listar() {
        return ResponseEntity.ok(denunciaService.listar(usuarioAutenticadoService.getUsuarioId()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<DenunciaResponse> moderar(@PathVariable Integer id, @Valid @RequestBody ModerarDenunciaRequest request) {
        return ResponseEntity.ok(denunciaService.moderar(usuarioAutenticadoService.getUsuarioId(), id, request));
    }
}
