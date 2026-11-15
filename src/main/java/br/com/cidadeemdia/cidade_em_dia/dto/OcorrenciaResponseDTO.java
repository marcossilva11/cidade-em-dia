package br.com.cidadeemdia.cidade_em_dia.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OcorrenciaResponseDTO {

    private Integer idOcorrencia;
    private String titulo;
    private String nomeUsuario;
    private String nomeBairro;
    private String nomeCategoria;
    private String descricao;
    private String foto;
    private String endereco;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String status;
    private String prioridade;
    private LocalDateTime dataRegistro;

    public OcorrenciaResponseDTO(Integer idOcorrencia, String titulo,
                                 String nomeUsuario, String nomeBairro,
                                 String nomeCategoria, String descricao,
                                 String foto, String endereco,
                                 BigDecimal latitude, BigDecimal longitude,
                                 String status, String prioridade,
                                 LocalDateTime dataRegistro) {

        this.idOcorrencia = idOcorrencia;
        this.titulo = titulo;
        this.nomeUsuario = nomeUsuario;
        this.nomeBairro = nomeBairro;
        this.nomeCategoria = nomeCategoria;
        this.descricao = descricao;
        this.foto = foto;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.prioridade = prioridade;
        this.dataRegistro = dataRegistro;
    }

    public Integer getIdOcorrencia() {
        return idOcorrencia;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public String getNomeBairro() {
        return nomeBairro;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getFoto() {
        return foto;
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

    public String getStatus() {
        return status;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }
}