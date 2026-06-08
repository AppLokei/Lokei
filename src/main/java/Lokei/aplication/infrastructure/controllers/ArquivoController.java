package Lokei.aplication.infrastructure.controllers;

import Lokei.aplication.application.dto.upload.ImagemUploadResponse;
import Lokei.aplication.application.service.UploadImagemService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/arquivos")
public class ArquivoController {

    private final UploadImagemService uploadImagemService;

    public ArquivoController(UploadImagemService uploadImagemService) {
        this.uploadImagemService = uploadImagemService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<ImagemUploadResponse>> upload(@RequestPart("files") List<MultipartFile> files) {
        return ResponseEntity.ok(uploadImagemService.upload(files));
    }

    @GetMapping(value = "/{filename:.+}")
    public ResponseEntity<Resource> download(@PathVariable String filename) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(uploadImagemService.carregarArquivo(filename));
    }
}
