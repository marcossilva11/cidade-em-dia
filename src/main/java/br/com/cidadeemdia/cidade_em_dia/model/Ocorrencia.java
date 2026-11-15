package br.com.cidadeemdia.cidade_em_dia.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "OCORRENCIA")
public class Ocorrencia {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_ocorrencia")
    @SequenceGenerator(
            name = "seq_ocorrencia",
            sequenceName = "SEQ_OCORRENCIA",
            allocationSize = 1
    )
    @Column(name = "ID_OCORRENCIA")
    private Integer idOcorrencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USUARIO_ID_USUARIO", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BAIRRO_ID_BAIRRO", nullable = false)
    private Bairro bairro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORIA_ID_CATEGORIA", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PRIORIDADE_ID_PRIORIDADE", nullable = false)
    private Prioridade prioridade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STATUS_ID_STATUS", nullable = false)
    private Status status;

    @Column(name = "TITULO_OCORRENCIA", nullable = false, length = 100)
    private String titulo;

    @Column(name = "DESCRICAO_OCORRENCIA", nullable = false, length = 500)
    private String descricao;

    @Column(name = "FOTO_OCORRENCIA", length = 500)
    private String foto;

    @Column(name = "DT_REGISTRO_OCORRENCIA", nullable = false)
    private LocalDateTime dataRegistro;

    @Column(name = "ENDERECO_OCORRENCIA", nullable = false, length = 200)
    private String endereco;

    @Column(name = "LATITUDE_OCORRENCIA", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "LONGITUDE_OCORRENCIA", precision = 10, scale = 7)
    private BigDecimal longitude;

    protected Ocorrencia() {
    }

    public Ocorrencia(
            Usuario usuario,
            Bairro bairro,
            Categoria categoria,
            Prioridade prioridade,
            Status status,
            String titulo,
            String descricao,
            String foto,
            String endereco,
            BigDecimal latitude,
            BigDecimal longitude) {

        this.usuario = usuario;
        this.bairro = bairro;
        this.categoria = categoria;
        this.prioridade = prioridade;
        this.status = status;
        this.titulo = titulo;
        this.descricao = descricao;
        this.foto = foto;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
        this.dataRegistro = LocalDateTime.now();
    }

    public void atualizarDados(
            Bairro bairro,
            Categoria categoria,
            String titulo,
            String descricao,
            String foto,
            String endereco,
            BigDecimal latitude,
            BigDecimal longitude) {

        this.bairro = bairro;
        this.categoria = categoria;
        this.titulo = titulo;
        this.descricao = descricao;
        this.foto = foto;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void alterarStatus(Status novoStatus) {
        if (novoStatus == null) {
            throw new IllegalArgumentException(
                    "O novo status não pode ser nulo."
            );
        }
        this.status = novoStatus;
    }

    public void alterarPrioridade(Prioridade novaPrioridade) {
        if (novaPrioridade == null) {
            throw new IllegalArgumentException(
                    "A nova prioridade não pode ser nula."
            );
        }
        this.prioridade = novaPrioridade;
    }

    public Integer getIdOcorrencia() {
        return idOcorrencia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Bairro getBairro() {
        return bairro;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public Status getStatus() {
        return status;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getFoto() {
        return foto;
    }

    public LocalDateTime getDataRegistro() {
        return dataRegistro;
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
}