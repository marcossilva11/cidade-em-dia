package br.com.cidadeemdia.cidade_em_dia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "BAIRRO")
public class Bairro {

    @Id
    @Column(name = "ID_BAIRRO")
    private Integer idBairro;

    @Column(name = "NOME_BAIRRO", nullable = false, length = 100)
    private String nome;

    @Column(name = "CIDADE_BAIRRO", nullable = false, length = 100)
    private String cidade;

    @Column(name = "ESTADO_BAIRRO", nullable = false, length = 2)
    private String estado;

    protected Bairro() {
    }

    public Integer getIdBairro() {
        return idBairro;
    }

    public String getNome() {
        return nome;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }
}