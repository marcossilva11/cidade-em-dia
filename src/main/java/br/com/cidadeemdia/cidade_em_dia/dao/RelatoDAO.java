package br.com.cidadeemdia.cidade_em_dia.dao;

import br.com.cidadeemdia.cidade_em_dia.model.Relato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RelatoDAO extends JpaRepository<Relato, Integer> {

    boolean existsByOcorrencia_IdOcorrenciaAndUsuario_IdUsuario(
            Integer idOcorrencia,
            Integer idUsuario
    );

    long countByOcorrencia_IdOcorrencia(Integer idOcorrencia);
}
