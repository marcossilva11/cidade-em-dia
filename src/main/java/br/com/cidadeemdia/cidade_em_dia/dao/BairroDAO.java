package br.com.cidadeemdia.cidade_em_dia.dao;

import br.com.cidadeemdia.cidade_em_dia.model.Bairro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BairroDAO extends JpaRepository<Bairro, Integer> {
}