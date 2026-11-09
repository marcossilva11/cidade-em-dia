package br.com.cidadeemdia.cidade_em_dia.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "OCORRENCIA")
public class Ocorrencia {

    @Id
    @Column(name = "ID_OCORRENCIA")
    private Integer idOcorrencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USUARIO", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CATEGORIA", nullable = false)
    private Categoria categoria;

    @Column(name = "DESCRICAO", nullable = false, length = 1000)
    private String descricao;

    @Column(name = "ENDERECO", nullable = false, length = 255)
    private String endereco;

    @Column(name = "LATITUDE", precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "LONGITUDE", precision = 10, scale = 7)
    private BigDecimal longitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false, length = 30)
    private StatusOcorrencia status;

    @Column(name = "GRAVIDADE", nullable = false)
    private Integer gravidade;

    @Column(name = "DATA_REGISTRO", nullable = false)
    private LocalDateTime dataRegistro;

    protected Ocorrencia() {
    }

    public Ocorrencia(
            Usuario usuario,
            Categoria categoria,
            String descricao,
            String endereco,
            BigDecimal latitude,
            BigDecimal longitude,
            Integer gravidade) {

        this.usuario = usuario;
        this.categoria = categoria;
        this.descricao = descricao;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gravidade = gravidade;
    }

    public void registrar() {
        this.status = StatusOcorrencia.PENDENTE;
        this.dataRegistro = LocalDateTime.now();
    }

    public void atualizarDados(
            Categoria categoria,
            String descricao,
            String endereco,
            BigDecimal latitude,
            BigDecimal longitude,
            Integer gravidade) {

        this.categoria = categoria;
        this.descricao = descricao;
        this.endereco = endereco;
        this.latitude = latitude;
        this.longitude = longitude;
        this.gravidade = gravidade;
    }

    public void alterarStatus(StatusOcorrencia novoStatus) {
        if (novoStatus == null) {
            throw new IllegalArgumentException(
                    "O novo status não pode ser nulo."
            );
        }

        this.status = novoStatus;
    }

    public Integer getIdOcorrencia() {
        return idOcorrencia;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Categoria getCategoria() {
        return categoria;
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
