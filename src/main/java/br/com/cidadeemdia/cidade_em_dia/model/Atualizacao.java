package br.com.cidadeemdia.cidade_em_dia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;

import java.time.LocalDateTime;

@Entity
@Table(name = "ATUALIZACAO")
public class Atualizacao {

    @Id
    @Column(name = "ID_ATUALIZACAO")
    private Integer idAtualizacao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_OCORRENCIA", nullable = false)
    private Ocorrencia ocorrencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_ANTERIOR", nullable = false, length = 30)
    private StatusOcorrencia statusAnterior;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS_NOVO", nullable = false, length = 30)
    private StatusOcorrencia statusNovo;

    @Column(name = "DESCRICAO", length = 1000)
    private String descricao;

    @Column(name = "DATA_ATUALIZACAO", nullable = false)
    private LocalDateTime dataAtualizacao;

    public Atualizacao() {
    }

    public Integer getIdAtualizacao() {
        return idAtualizacao;
    }

    public void setIdAtualizacao(Integer idAtualizacao) {
        this.idAtualizacao = idAtualizacao;
    }

    public Ocorrencia getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(Ocorrencia ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public StatusOcorrencia getStatusAnterior() {
        return statusAnterior;
    }

    public void setStatusAnterior(StatusOcorrencia statusAnterior) {
        this.statusAnterior = statusAnterior;
    }

    public StatusOcorrencia getStatusNovo() {
        return statusNovo;
    }

    public void setStatusNovo(StatusOcorrencia statusNovo) {
        this.statusNovo = statusNovo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
}
