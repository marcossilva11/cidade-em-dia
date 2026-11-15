package br.com.cidadeemdia.cidade_em_dia.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "RELATO",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_RELATO_USUARIO_OCORRENCIA",
                        columnNames = {"OCORRENCIA_ID_OCORRENCIA", "USUARIO_ID_USUARIO"}
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
    @JoinColumn(name = "OCORRENCIA_ID_OCORRENCIA", nullable = false)
    private Ocorrencia ocorrencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID_USUARIO", nullable = false)
    private Usuario usuario;

    @Column(name = "DT_REGISTRO_RELATO", nullable = false)
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