package br.com.cidadeemdia.cidade_em_dia.bo;

import br.com.cidadeemdia.cidade_em_dia.dao.OcorrenciaDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.RelatoDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.UsuarioDAO;

import br.com.cidadeemdia.cidade_em_dia.model.Ocorrencia;
import br.com.cidadeemdia.cidade_em_dia.model.Relato;
import br.com.cidadeemdia.cidade_em_dia.model.Usuario;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.cidadeemdia.cidade_em_dia.exception.ConflitoException;
import br.com.cidadeemdia.cidade_em_dia.exception.DadosInvalidosException;
import br.com.cidadeemdia.cidade_em_dia.exception.RecursoNaoEncontradoException;

@Service
public class RelatoBO {

    private final RelatoDAO relatoDAO;
    private final OcorrenciaDAO ocorrenciaDAO;
    private final UsuarioDAO usuarioDAO;

    public RelatoBO(
            RelatoDAO relatoDAO,
            OcorrenciaDAO ocorrenciaDAO,
            UsuarioDAO usuarioDAO) {

        this.relatoDAO = relatoDAO;
        this.ocorrenciaDAO = ocorrenciaDAO;
        this.usuarioDAO = usuarioDAO;
    }

    @Transactional
    public Relato confirmarOcorrencia(Integer idOcorrencia, Integer idUsuario) {

        if (idOcorrencia == null || idUsuario == null) {
            throw new DadosInvalidosException(
                    "Os IDs da ocorrência e do usuário são obrigatórios."
            );
        }

        Ocorrencia ocorrencia = ocorrenciaDAO.findById(idOcorrencia)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Ocorrência não encontrada."));

        Usuario usuario = usuarioDAO.findById(idUsuario)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado."));

        boolean jaConfirmou =
                relatoDAO.existsByOcorrencia_IdOcorrenciaAndUsuario_IdUsuario(
                        idOcorrencia,
                        idUsuario
                );

        if (jaConfirmou) {
            throw new ConflitoException(
                    "Esse usuário já confirmou essa ocorrência."
            );
        }

        Relato relato = new Relato(ocorrencia, usuario);

        return relatoDAO.save(relato);
    }
}
