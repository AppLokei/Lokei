package Lokei.aplication.infrastructure.controllers;

import Lokei.aplication.application.dto.aluguel.*;
import Lokei.aplication.application.service.AluguelService;
import Lokei.aplication.infrastructure.persistence.enums.papelUsuarioEnum;
import Lokei.aplication.infrastructure.security.UsuarioAutenticadoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alugueis")
public class AluguelController {

    private final AluguelService aluguelService;
    private final UsuarioAutenticadoService usuarioAutenticadoService;

    public AluguelController(AluguelService aluguelService, UsuarioAutenticadoService usuarioAutenticadoService) {
        this.aluguelService = aluguelService;
        this.usuarioAutenticadoService = usuarioAutenticadoService;
    }

    @GetMapping("/meus")
    public ResponseEntity<List<AluguelResumoResponse>> meus() {
        return ResponseEntity.ok(aluguelService.listarDoLocatario(usuarioAutenticadoService.getUsuarioId()));
    }

    @GetMapping("/recebidos")
    public ResponseEntity<List<AluguelResumoResponse>> recebidos() {
        return ResponseEntity.ok(aluguelService.listarDoLocador(usuarioAutenticadoService.getUsuarioId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AluguelDetalheResponse> detalhar(@PathVariable Integer id) {
        var usuario = usuarioAutenticadoService.getUsuarioAutenticado();
        boolean admin = usuario.getUsuario().getPapel() == papelUsuarioEnum.ADMIN;
        return ResponseEntity.ok(aluguelService.detalhar(id, usuario.getId(), admin));
    }

    @PatchMapping("/{id}/aprovar")
    public ResponseEntity<AluguelDetalheResponse> aprovar(@PathVariable Integer id) {
        return ResponseEntity.ok(aluguelService.aprovar(id, usuarioAutenticadoService.getUsuarioId()));
    }

    @PatchMapping("/{id}/confirmar-entrega")
    public ResponseEntity<AluguelDetalheResponse> confirmarEntrega(@PathVariable Integer id) {
        return ResponseEntity.ok(aluguelService.confirmarEntrega(id, usuarioAutenticadoService.getUsuarioId()));
    }

    @PatchMapping("/{id}/finalizar")
    public ResponseEntity<AluguelDetalheResponse> finalizar(@PathVariable Integer id) {
        return ResponseEntity.ok(aluguelService.finalizar(id, usuarioAutenticadoService.getUsuarioId()));
    }

    @PatchMapping("/{id}/reprovar")
    public ResponseEntity<AluguelDetalheResponse> reprovar(@PathVariable Integer id, @Valid @RequestBody ReprovarAluguelRequest request) {
        return ResponseEntity.ok(aluguelService.reprovar(id, usuarioAutenticadoService.getUsuarioId(), request.motivo()));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<AluguelDetalheResponse> cancelar(@PathVariable Integer id, @Valid @RequestBody CancelarAluguelRequest request) {
        return ResponseEntity.ok(aluguelService.cancelar(id, usuarioAutenticadoService.getUsuarioId(), request.motivo()));
    }
}
