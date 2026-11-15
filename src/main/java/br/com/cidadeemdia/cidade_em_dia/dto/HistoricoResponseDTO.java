package br.com.cidadeemdia.cidade_em_dia.dto;

import java.time.LocalDateTime;

public class HistoricoResponseDTO {

    private Integer idHistorico;
    private String nomeUsuario;
    private String status;
    private String observacao;
    private LocalDateTime dataAlteracao;

    public HistoricoResponseDTO(Integer idHistorico, String nomeUsuario,
                                String status, String observacao,
                                LocalDateTime dataAlteracao) {

        this.idHistorico = idHistorico;
        this.nomeUsuario = nomeUsuario;
        this.status = status;
        this.observacao = observacao;
        this.dataAlteracao = dataAlteracao;
    }

    public Integer getIdHistorico() {
        return idHistorico;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getStatus() {
        return status;
    }

    public String getObservacao() {
        return observacao;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }
}