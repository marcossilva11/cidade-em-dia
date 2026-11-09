package br.com.cidadeemdia.cidade_em_dia.dao;

import br.com.cidadeemdia.cidade_em_dia.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioDAO extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
