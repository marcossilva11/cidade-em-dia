package br.com.cidadeemdia.cidade_em_dia.dto;

import br.com.cidadeemdia.cidade_em_dia.model.StatusOcorrencia;

public class StatusRequestDTO {

    private Integer idUsuario;
    private StatusOcorrencia status;
    private String descricao;

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public StatusOcorrencia getStatus() {
        return status;
    }

    public void setStatus(StatusOcorrencia status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}