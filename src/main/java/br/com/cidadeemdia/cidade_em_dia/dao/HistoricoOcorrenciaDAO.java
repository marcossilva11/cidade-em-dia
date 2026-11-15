package br.com.cidadeemdia.cidade_em_dia.dao;

import br.com.cidadeemdia.cidade_em_dia.model.HistoricoOcorrencia;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricoOcorrenciaDAO extends JpaRepository<HistoricoOcorrencia, Integer> {

    @EntityGraph(attributePaths = {"usuario", "status"})
    List<HistoricoOcorrencia>
    findByOcorrencia_IdOcorrenciaOrderByDataAlteracaoDescIdHistoricoDesc(
            Integer idOcorrencia
    );
}