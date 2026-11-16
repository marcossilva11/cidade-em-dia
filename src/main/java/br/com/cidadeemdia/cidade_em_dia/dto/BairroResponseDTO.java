package br.com.cidadeemdia.cidade_em_dia.dto;

public class BairroResponseDTO {

    private Integer idBairro;
    private String nome;
    private String cidade;
    private String estado;

    public BairroResponseDTO(Integer idBairro, String nome, String cidade, String estado) {
        this.idBairro = idBairro;
        this.nome = nome;
        this.cidade = cidade;
        this.estado = estado;
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