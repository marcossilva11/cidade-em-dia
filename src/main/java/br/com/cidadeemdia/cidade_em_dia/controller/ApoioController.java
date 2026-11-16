package br.com.cidadeemdia.cidade_em_dia.controller;

import br.com.cidadeemdia.cidade_em_dia.bo.ApoioBO;
import br.com.cidadeemdia.cidade_em_dia.dto.BairroResponseDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.CategoriaResponseDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.OpcaoResponseDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ApoioController {

    private final ApoioBO apoioBO;

    public ApoioController(ApoioBO apoioBO) {
        this.apoioBO = apoioBO;
    }

    // GET /categorias
    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        return ResponseEntity.ok(apoioBO.listarCategorias());
    }

    // GET /bairros
    @GetMapping("/bairros")
    public ResponseEntity<List<BairroResponseDTO>> listarBairros() {
        return ResponseEntity.ok(apoioBO.listarBairros());
    }

    // GET /status
    @GetMapping("/status")
    public ResponseEntity<List<OpcaoResponseDTO>> listarStatus() {
        return ResponseEntity.ok(apoioBO.listarStatus());
    }

    // GET /prioridades
    @GetMapping("/prioridades")
    public ResponseEntity<List<OpcaoResponseDTO>> listarPrioridades() {
        return ResponseEntity.ok(apoioBO.listarPrioridades());
    }
}