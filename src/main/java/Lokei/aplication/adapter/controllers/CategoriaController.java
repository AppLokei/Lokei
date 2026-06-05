package Lokei.aplication.adapter.controllers;

import Lokei.aplication.domain.enums.CategoriaEnum;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @GetMapping
    public ResponseEntity<List<CategoriaEnum>> listar() {
        return ResponseEntity.ok(
                Arrays.asList(CategoriaEnum.values())
        );
    }
}
