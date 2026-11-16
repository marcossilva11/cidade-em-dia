package br.com.cidadeemdia.cidade_em_dia.dto;

public class CategoriaResponseDTO {

    private Integer idCategoria;
    private String nome;
    private String descricao;

    public CategoriaResponseDTO(Integer idCategoria, String nome, String descricao) {
        this.idCategoria = idCategoria;
        this.nome = nome;
        this.descricao = descricao;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}