package br.com.cidadeemdia.cidade_em_dia.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ATUALIZACAO")
public class Atualizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_atualizacao")
    @SequenceGenerator(
            name = "seq_atualizacao",
            sequenceName = "SEQ_ATUALIZACAO",
            allocationSize = 1
    )
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

    protected Atualizacao() {
    }

    public Atualizacao(
            Ocorrencia ocorrencia,
            Usuario usuario,
            StatusOcorrencia statusAnterior,
            StatusOcorrencia statusNovo,
            String descricao) {

        this.ocorrencia = ocorrencia;
        this.usuario = usuario;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.descricao = descricao;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Integer getIdAtualizacao() {
        return idAtualizacao;
    }

    public Ocorrencia getOcorrencia() {
        return ocorrencia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public StatusOcorrencia getStatusAnterior() {
        return statusAnterior;
    }

    public StatusOcorrencia getStatusNovo() {
        return statusNovo;
    }

    public String getDescricao() {
        return descricao;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
}
