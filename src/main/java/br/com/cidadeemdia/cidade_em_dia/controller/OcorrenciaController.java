package br.com.cidadeemdia.cidade_em_dia.controller;

import br.com.cidadeemdia.cidade_em_dia.bo.OcorrenciaBO;
import br.com.cidadeemdia.cidade_em_dia.dto.HistoricoResponseDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.OcorrenciaRequestDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.OcorrenciaResponseDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.StatusRequestDTO;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ocorrencias")
public class OcorrenciaController {

    private final OcorrenciaBO ocorrenciaBO;

    public OcorrenciaController(OcorrenciaBO ocorrenciaBO) {
        this.ocorrenciaBO = ocorrenciaBO;
    }

    // GET /ocorrencias?ordenarPor=prioridade&status=PENDENTE
    @GetMapping
    public ResponseEntity<List<OcorrenciaResponseDTO>> listar(
            @RequestParam(name = "ordenarPor", required = false) String ordenarPor,
            @RequestParam(name = "status", required = false) String status) {

        return ResponseEntity.ok(ocorrenciaBO.listarTodas(ordenarPor, status));
    }

    // GET /ocorrencias/{id}
    @GetMapping("/{id}")
    public ResponseEntity<OcorrenciaResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(ocorrenciaBO.consultarPorId(id));
    }

    // POST /ocorrencias
    @PostMapping
    public ResponseEntity<OcorrenciaResponseDTO> cadastrar(
            @RequestBody OcorrenciaRequestDTO dto) {

        OcorrenciaResponseDTO criada = ocorrenciaBO.cadastrar(dto);

        return ResponseEntity
                .created(URI.create("/ocorrencias/" + criada.getIdOcorrencia()))
                .body(criada);
    }

    // PUT /ocorrencias/{id}
    @PutMapping("/{id}")
    public ResponseEntity<OcorrenciaResponseDTO> atualizar(
            @PathVariable Integer id,
            @RequestBody OcorrenciaRequestDTO dto) {

        return ResponseEntity.ok(ocorrenciaBO.atualizar(id, dto));
    }

    // DELETE /ocorrencias/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Integer id) {
        ocorrenciaBO.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // PUT /ocorrencias/{id}/status
    @PutMapping("/{id}/status")
    public ResponseEntity<OcorrenciaResponseDTO> atualizarStatus(
            @PathVariable Integer id,
            @RequestBody StatusRequestDTO dto) {

        return ResponseEntity.ok(
                ocorrenciaBO.atualizarStatus(
                        id,
                        dto.getStatus(),
                        dto.getIdUsuario(),
                        dto.getDescricao()
                )
        );
    }

    // POST /ocorrencias/recalcular-prioridades?idUsuario=1  (somente ADMIN)
    @PostMapping("/recalcular-prioridades")
    public ResponseEntity<Map<String, Integer>> recalcularPrioridades(
            @RequestParam(name = "idUsuario", required = false) Integer idUsuario) {

        return ResponseEntity.ok(
                Map.of("atualizadas", ocorrenciaBO.recalcularPrioridades(idUsuario))
        );
    }

    // GET /ocorrencias/{id}/historico
    @GetMapping("/{id}/historico")
    public ResponseEntity<List<HistoricoResponseDTO>> listarHistorico(
            @PathVariable Integer id) {

        return ResponseEntity.ok(ocorrenciaBO.listarHistorico(id));
    }
}