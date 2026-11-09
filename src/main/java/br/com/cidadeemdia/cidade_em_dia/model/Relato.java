package br.com.cidadeemdia.cidade_em_dia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.UniqueConstraint;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "RELATO",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"ID_OCORRENCIA", "ID_USUARIO"}
                )
        }
)
public class Relato {

    @Id
    @Column(name = "ID_RELATO")
    private Integer idRelato;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_OCORRENCIA", nullable = false)
    private Ocorrencia ocorrencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @Column(name = "DATA_REGISTRO", nullable = false)
    private LocalDateTime dataRegistro;

    public Relato() {
    }

    public Integer getIdRelato() {
        return idRelato;
    }

    public void setIdRelato(Integer idRelato) {
        this.idRelato = idRelato;
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

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(LocalDateTime dataRegistro) {
        this.dataRegistro = dataRegistro;
    }
}
