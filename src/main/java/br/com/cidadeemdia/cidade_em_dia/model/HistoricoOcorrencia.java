package br.com.cidadeemdia.cidade_em_dia.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Registro de cada mudança de status de uma ocorrência
 * (inclusive o status inicial, gravado no cadastro).
 * Substitui a antiga classe Atualizacao.
 */
@Entity
@Table(name = "HISTORICO_OCORRENCIA")
public class HistoricoOcorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_historico_ocorrencia")
    @SequenceGenerator(
            name = "seq_historico_ocorrencia",
            sequenceName = "SEQ_HISTORICO_OCORRENCIA",
            allocationSize = 1
    )
    @Column(name = "ID_HISTORICO")
    private Integer idHistorico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OCORRENCIA_ID_OCORRENCIA", nullable = false)
    private Ocorrencia ocorrencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID_USUARIO", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS_ID_STATUS", nullable = false)
    private Status status;

    @Column(name = "DT_ALTERACAO_HISTORICO", nullable = false)
    private LocalDateTime dataAlteracao;

    @Column(name = "OBSERVACAO_HISTORICO", nullable = false, length = 500)
    private String observacao;

    protected HistoricoOcorrencia() {
    }

    public HistoricoOcorrencia(
            Ocorrencia ocorrencia,
            Usuario usuario,
            Status status,
            String observacao) {

        this.ocorrencia = ocorrencia;
        this.usuario = usuario;
        this.status = status;
        this.observacao = observacao;
        this.dataAlteracao = LocalDateTime.now();
    }

    public Integer getIdHistorico() {
        return idHistorico;
    }

    public Ocorrencia getOcorrencia() {
        return ocorrencia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public String getObservacao() {
        return observacao;
    }
}