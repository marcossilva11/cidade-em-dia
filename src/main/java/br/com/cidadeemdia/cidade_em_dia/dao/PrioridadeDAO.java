package br.com.cidadeemdia.cidade_em_dia.dao;

import br.com.cidadeemdia.cidade_em_dia.model.Prioridade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PrioridadeDAO extends JpaRepository<Prioridade, Integer> {

    Optional<Prioridade> findByNome(String nome);
}