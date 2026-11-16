package br.com.cidadeemdia.cidade_em_dia.dto;

import java.time.LocalDateTime;

public class UsuarioResponseDTO {

    private Integer idUsuario;
    private String nome;
    private String email;
    private String telefone;
    private String tipo;
    private LocalDateTime dataCadastro;

    public UsuarioResponseDTO(Integer idUsuario, String nome, String email,
                              String telefone, String tipo,
                              LocalDateTime dataCadastro) {

        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.tipo = tipo;
        this.dataCadastro = dataCadastro;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
}