package br.com.cidadeemdia.cidade_em_dia.dto;

import br.com.cidadeemdia.cidade_em_dia.model.StatusOcorrencia;

import java.time.LocalDateTime;

public class AtualizacaoResponseDTO {

    private Integer idAtualizacao;
    private String nomeUsuario;
    private StatusOcorrencia statusAnterior;
    private StatusOcorrencia statusNovo;
    private String descricao;
    private LocalDateTime dataAtualizacao;

    public AtualizacaoResponseDTO(Integer idAtualizacao, String nomeUsuario,
                                  StatusOcorrencia statusAnterior,
                                  StatusOcorrencia statusNovo,
                                  String descricao,
                                  LocalDateTime dataAtualizacao) {

        this.idAtualizacao = idAtualizacao;
        this.nomeUsuario = nomeUsuario;
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.descricao = descricao;
        this.dataAtualizacao = dataAtualizacao;
    }

    public Integer getIdAtualizacao() {
        return idAtualizacao;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
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