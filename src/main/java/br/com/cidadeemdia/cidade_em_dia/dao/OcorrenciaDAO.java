package br.com.cidadeemdia.cidade_em_dia.dao;

import br.com.cidadeemdia.cidade_em_dia.model.Ocorrencia;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OcorrenciaDAO extends JpaRepository<Ocorrencia, Integer> {

    @EntityGraph(attributePaths = {"usuario", "bairro", "categoria", "prioridade", "status"})
    List<Ocorrencia> findAllByOrderByDataRegistroDesc();

    @EntityGraph(attributePaths = {"usuario", "bairro", "categoria", "prioridade", "status"})
    List<Ocorrencia> findByStatus_NomeOrderByDataRegistroDesc(String nomeStatus);
}