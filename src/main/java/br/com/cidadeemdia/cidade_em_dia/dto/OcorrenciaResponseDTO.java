package br.com.cidadeemdia.cidade_em_dia.dto;

import br.com.cidadeemdia.cidade_em_dia.model.StatusOcorrencia;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OcorrenciaResponseDTO {

    private Integer idOcorrencia;
    private String nomeUsuario;
    private String nomeCategoria;
    private String descricao;
    private String endereco;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private StatusOcorrencia status;
    private Integer gravidade;
    private LocalDateTime dataRegistro;

    public OcorrenciaResponseDTO(Integer idOcorrencia, String nomeUsuario,
                                 String nomeCategoria, String descricao,
                                 String endereco, BigDecimal latitude,
                                 BigDecimal longitude, StatusOcorrencia status,
                                 Integer gravidade, LocalDateTime dataRegistro) {

        this.idOcorrencia = idOcorrencia;
        this.nomeUsuario = nomeUsuario;
        this.nomeCategoria = nomeCategoria;
        this.descricao = descricao;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.gravidade = gravidade;
        this.dataRegistro = dataRegistro;
    }

    public Integer getIdOcorrencia() {
        return idOcorrencia;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public StatusOcorrencia getStatus() {
        return status;
    }

    public Integer getGravidade() {
        return gravidade;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }
}