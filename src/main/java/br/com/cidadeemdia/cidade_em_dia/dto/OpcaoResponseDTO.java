package br.com.cidadeemdia.cidade_em_dia.dto;

public class OpcaoResponseDTO {

    private Integer id;
    private String nome;
    private String descricao;

    public OpcaoResponseDTO(Integer id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}