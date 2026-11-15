package br.com.cidadeemdia.cidade_em_dia.dto;

public class StatusRequestDTO {

    private Integer idUsuario;
    private String status;      // nome do status: PENDENTE, EM_ANDAMENTO ou RESOLVIDO
    private String descricao;   // observação gravada no histórico (opcional)

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}