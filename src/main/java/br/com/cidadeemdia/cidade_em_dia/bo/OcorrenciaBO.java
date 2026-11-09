package br.com.cidadeemdia.cidade_em_dia.bo;

import br.com.cidadeemdia.cidade_em_dia.dao.AtualizacaoDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.CategoriaDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.OcorrenciaDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.RelatoDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.UsuarioDAO;

import br.com.cidadeemdia.cidade_em_dia.model.Atualizacao;
import br.com.cidadeemdia.cidade_em_dia.model.Categoria;
import br.com.cidadeemdia.cidade_em_dia.model.Ocorrencia;
import br.com.cidadeemdia.cidade_em_dia.model.StatusOcorrencia;
import br.com.cidadeemdia.cidade_em_dia.model.Usuario;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import br.com.cidadeemdia.cidade_em_dia.exception.ConflitoException;
import br.com.cidadeemdia.cidade_em_dia.exception.DadosInvalidosException;
import br.com.cidadeemdia.cidade_em_dia.exception.RecursoNaoEncontradoException;

@Service
public class OcorrenciaBO {

    private final OcorrenciaDAO ocorrenciaDAO;
    private final RelatoDAO relatoDAO;
    private final AtualizacaoDAO atualizacaoDAO;
    private final UsuarioDAO usuarioDAO;
    private final CategoriaDAO categoriaDAO;

    public OcorrenciaBO(
            OcorrenciaDAO ocorrenciaDAO,
            RelatoDAO relatoDAO,
            AtualizacaoDAO atualizacaoDAO,
            UsuarioDAO usuarioDAO,
            CategoriaDAO categoriaDAO) {

        this.ocorrenciaDAO = ocorrenciaDAO;
        this.relatoDAO = relatoDAO;
        this.atualizacaoDAO = atualizacaoDAO;
        this.usuarioDAO = usuarioDAO;
        this.categoriaDAO = categoriaDAO;
    }

    @Transactional(readOnly = true)
    public List<Ocorrencia> listarTodas() {
        return ocorrenciaDAO.findAllByOrderByDataRegistroDesc();
    }

    @Transactional(readOnly = true)
    public Ocorrencia buscarPorId(Integer id) {
        return ocorrenciaDAO.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Ocorrência não encontrada."));
    }

    @Transactional
    public Ocorrencia cadastrar(Ocorrencia dados) {

        validarOcorrencia(dados);

        Usuario usuario = buscarUsuario(dados.getUsuario().getIdUsuario());
        Categoria categoria = buscarCategoria(dados.getCategoria().getIdCategoria());

        Ocorrencia ocorrencia = new Ocorrencia(
                usuario,
                categoria,
                dados.getDescricao(),
                dados.getEndereco(),
                dados.getLatitude(),
                dados.getLongitude(),
                dados.getGravidade()
        );

        ocorrencia.registrar();

        return ocorrenciaDAO.save(ocorrencia);
    }

    @Transactional
    public Ocorrencia atualizar(Integer id, Ocorrencia dados) {

        validarOcorrencia(dados);

        Ocorrencia ocorrencia = buscarPorId(id);

        Categoria categoria = buscarCategoria(
                dados.getCategoria().getIdCategoria()
        );

        ocorrencia.atualizarDados(
                categoria,
                dados.getDescricao(),
                dados.getEndereco(),
                dados.getLatitude(),
                dados.getLongitude(),
                dados.getGravidade()
        );

        return ocorrenciaDAO.save(ocorrencia);
    }

    @Transactional
    public void excluir(Integer id) {

        Ocorrencia ocorrencia = buscarPorId(id);

        long quantidadeRelatos =
                relatoDAO.countByOcorrencia_IdOcorrencia(id);

        List<Atualizacao> historico =
                atualizacaoDAO
                        .findByOcorrencia_IdOcorrenciaOrderByDataAtualizacaoDesc(id);

        if (quantidadeRelatos > 0 || !historico.isEmpty()) {
            throw new ConflitoException(
                    "Não é possível excluir uma ocorrência que possui relatos ou histórico."
            );
        }

        ocorrenciaDAO.delete(ocorrencia);
    }

    @Transactional
    public Ocorrencia atualizarStatus(
            Integer id,
            StatusOcorrencia novoStatus,
            Integer idUsuario,
            String descricao) {

        if (novoStatus == null) {
            throw new DadosInvalidosException("O novo status é obrigatório.");
        }

        Ocorrencia ocorrencia = buscarPorId(id);

        if (ocorrencia.getStatus() == novoStatus) {
            throw new DadosInvalidosException(
                    "A ocorrência já possui esse status."
            );
        }

        Usuario usuario = buscarUsuario(idUsuario);

        StatusOcorrencia statusAnterior = ocorrencia.getStatus();

        ocorrencia.alterarStatus(novoStatus);

        Atualizacao atualizacao = new Atualizacao(
                ocorrencia,
                usuario,
                statusAnterior,
                novoStatus,
                descricao
        );

        ocorrenciaDAO.save(ocorrencia);
        atualizacaoDAO.save(atualizacao);

        return ocorrencia;
    }

    @Transactional(readOnly = true)
    public List<Atualizacao> listarHistorico(Integer id) {

        buscarPorId(id);

        return atualizacaoDAO
                .findByOcorrencia_IdOcorrenciaOrderByDataAtualizacaoDesc(id);
    }

    private void validarOcorrencia(Ocorrencia ocorrencia) {

        if (ocorrencia == null) {
            throw new DadosInvalidosException(
                    "Os dados da ocorrência são obrigatórios."
            );
        }

        if (ocorrencia.getUsuario() == null
                || ocorrencia.getUsuario().getIdUsuario() == null) {
            throw new DadosInvalidosException("O usuário é obrigatório.");
        }

        if (ocorrencia.getCategoria() == null
                || ocorrencia.getCategoria().getIdCategoria() == null) {
            throw new DadosInvalidosException("A categoria é obrigatória.");
        }

        if (ocorrencia.getDescricao() == null
                || ocorrencia.getDescricao().isBlank()) {
            throw new DadosInvalidosException("A descrição é obrigatória.");
        }

        if (ocorrencia.getEndereco() == null
                || ocorrencia.getEndereco().isBlank()) {
            throw new DadosInvalidosException("O endereço é obrigatório.");
        }

        if (ocorrencia.getGravidade() == null
                || ocorrencia.getGravidade() < 1
                || ocorrencia.getGravidade() > 5) {
            throw new DadosInvalidosException(
                    "A gravidade deve estar entre 1 e 5."
            );
        }
    }

    private Usuario buscarUsuario(Integer id) {

        if (id == null) {
            throw new DadosInvalidosException("O ID do usuário é obrigatório.");
        }

        return usuarioDAO.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Usuário não encontrado."));
    }

    private Categoria buscarCategoria(Integer id) {

        if (id == null) {
            throw new DadosInvalidosException("O ID da categoria é obrigatório.");
        }

        return categoriaDAO.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Categoria não encontrada."));
    }
}
