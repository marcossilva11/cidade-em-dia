package br.com.cidadeemdia.cidade_em_dia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "STATUS")
public class Status {

    @Id
    @Column(name = "ID_STATUS")
    private Integer idStatus;

    @Column(name = "NOME_STATUS", nullable = false, unique = true, length = 30)
    private String nome;

    @Column(name = "DESCRICAO_STATUS", length = 200)
    private String descricao;

    protected Status() {
    }

    public Integer getIdStatus() {
        return idStatus;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}