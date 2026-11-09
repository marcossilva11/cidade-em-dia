package br.com.cidadeemdia.cidade_em_dia.dao;

import br.com.cidadeemdia.cidade_em_dia.model.Atualizacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AtualizacaoDAO extends JpaRepository<Atualizacao, Integer> {

    List<Atualizacao> findByOcorrencia_IdOcorrenciaOrderByDataAtualizacaoDesc(
            Integer idOcorrencia
    );
}
