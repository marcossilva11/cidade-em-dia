package br.com.cidadeemdia.cidade_em_dia.controller;

import br.com.cidadeemdia.cidade_em_dia.bo.RelatoBO;
import br.com.cidadeemdia.cidade_em_dia.dto.RelatoRequestDTO;
import br.com.cidadeemdia.cidade_em_dia.model.Relato;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ocorrencias")
public class RelatoController {

    private final RelatoBO relatoBO;

    public RelatoController(RelatoBO relatoBO) {
        this.relatoBO = relatoBO;
    }

    // POST /ocorrencias/{id}/relatos
    @PostMapping("/{id}/relatos")
    public ResponseEntity<Void> confirmar(
            @PathVariable Integer id,
            @RequestBody RelatoRequestDTO dto) {

        relatoBO.confirmarOcorrencia(
                id,
                dto.getIdUsuario()
        );

        return ResponseEntity.status(201).build();
    }
}
