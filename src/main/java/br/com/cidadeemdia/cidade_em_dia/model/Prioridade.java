package br.com.cidadeemdia.cidade_em_dia.model;

import jakarta.persistence.*;

@Entity
@Table(name = "PRIORIDADE")
public class Prioridade {

    @Id
    @Column(name = "ID_PRIORIDADE")
    private Integer idPrioridade;

    @Column(name = "NOME_PRIORIDADE", nullable = false, unique = true, length = 20)
    private String nome;

    @Column(name = "DESCRICAO_PRIORIDADE", length = 200)
    private String descricao;

    protected Prioridade() {
    }

    public Integer getIdPrioridade() {
        return idPrioridade;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}