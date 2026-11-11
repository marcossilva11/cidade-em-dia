package br.com.cidadeemdia.cidade_em_dia.model;

import jakarta.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_relato")
    @SequenceGenerator(
            name = "seq_relato",
            sequenceName = "SEQ_RELATO",
            allocationSize = 1
    )
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

    protected Relato() {
    }

    public Relato(Ocorrencia ocorrencia, Usuario usuario) {
        this.ocorrencia = ocorrencia;
        this.usuario = usuario;
        this.dataRegistro = LocalDateTime.now();
    }

    public Integer getIdRelato() {
        return idRelato;
    }

    public Ocorrencia getOcorrencia() {
        return ocorrencia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
    }
}
