package br.com.cidadeemdia.cidade_em_dia.dao;

import br.com.cidadeemdia.cidade_em_dia.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StatusDAO extends JpaRepository<Status, Integer> {

    Optional<Status> findByNome(String nome);
}