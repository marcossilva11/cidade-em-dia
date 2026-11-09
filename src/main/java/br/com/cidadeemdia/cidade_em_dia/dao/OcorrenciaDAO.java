package br.com.cidadeemdia.cidade_em_dia.dao;

import br.com.cidadeemdia.cidade_em_dia.model.Ocorrencia;
import br.com.cidadeemdia.cidade_em_dia.model.StatusOcorrencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OcorrenciaDAO extends JpaRepository<Ocorrencia, Integer> {

    List<Ocorrencia> findByStatusOrderByDataRegistroDesc(
            StatusOcorrencia status
    );

    List<Ocorrencia> findAllByOrderByDataRegistroDesc();
}
