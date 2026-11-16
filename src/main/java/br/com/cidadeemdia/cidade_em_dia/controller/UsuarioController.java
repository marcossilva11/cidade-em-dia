package br.com.cidadeemdia.cidade_em_dia.controller;

import br.com.cidadeemdia.cidade_em_dia.bo.UsuarioBO;
import br.com.cidadeemdia.cidade_em_dia.dto.LoginRequestDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.UsuarioRequestDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.UsuarioResponseDTO;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioBO usuarioBO;

    public UsuarioController(UsuarioBO usuarioBO) {
        this.usuarioBO = usuarioBO;
    }

    // POST /usuarios
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> cadastrar(
            @RequestBody UsuarioRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioBO.cadastrar(dto));
    }

    // POST /usuarios/login
    @PostMapping("/login")
    public ResponseEntity<UsuarioResponseDTO> login(
            @RequestBody LoginRequestDTO dto) {

        return ResponseEntity.ok(usuarioBO.autenticar(dto));
    }
}