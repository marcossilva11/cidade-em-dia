package br.com.cidadeemdia.cidade_em_dia.bo;

import br.com.cidadeemdia.cidade_em_dia.dao.AtualizacaoDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.CategoriaDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.OcorrenciaDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.RelatoDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.UsuarioDAO;

import br.com.cidadeemdia.cidade_em_dia.dto.AtualizacaoResponseDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.OcorrenciaRequestDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.OcorrenciaResponseDTO;

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
    public List<OcorrenciaResponseDTO> listarTodas() {
        return ocorrenciaDAO.findAllByOrderByDataRegistroDesc()
                .stream()
                .map(this::paraResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OcorrenciaResponseDTO consultarPorId(Integer id) {
        return paraResponse(buscarPorId(id));
    }

    @Transactional
    public OcorrenciaResponseDTO cadastrar(OcorrenciaRequestDTO dto) {

        validarOcorrencia(dto);

        if (dto.getIdUsuario() == null) {
            throw new DadosInvalidosException("O ID do usuário é obrigatório.");
        }

        Usuario usuario = buscarUsuario(dto.getIdUsuario());
        Categoria categoria = buscarCategoria(dto.getIdCategoria());

        Ocorrencia ocorrencia = new Ocorrencia(
                usuario,
                categoria,
                dto.getDescricao(),
                dto.getEndereco(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getGravidade()
        );


        ocorrencia.registrar();

        return paraResponse(ocorrenciaDAO.save(ocorrencia));
    }

    @Transactional
    public OcorrenciaResponseDTO atualizar(Integer id, OcorrenciaRequestDTO dto) {

        validarOcorrencia(dto);

        Ocorrencia ocorrencia = buscarPorId(id);
        Categoria categoria = buscarCategoria(dto.getIdCategoria());

        ocorrencia.atualizarDados(
                categoria,
                dto.getDescricao(),
                dto.getEndereco(),
                dto.getLatitude(),
                dto.getLongitude(),
                dto.getGravidade()
        );

        return paraResponse(ocorrenciaDAO.save(ocorrencia));
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
    public OcorrenciaResponseDTO atualizarStatus(
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

        return paraResponse(ocorrencia);
    }

    @Transactional(readOnly = true)
    public List<AtualizacaoResponseDTO> listarHistorico(Integer id) {

        buscarPorId(id);

        return atualizacaoDAO
                .findByOcorrencia_IdOcorrenciaOrderByDataAtualizacaoDesc(id)
                .stream()
                .map(this::paraResponse)
                .toList();
    }

    private OcorrenciaResponseDTO paraResponse(Ocorrencia o) {
        return new OcorrenciaResponseDTO(
                o.getIdOcorrencia(),
                o.getUsuario().getNome(),
                o.getCategoria().getNome(),
                o.getDescricao(),
                o.getEndereco(),
                o.getLatitude(),
                o.getLongitude(),
                o.getStatus(),
                o.getGravidade(),
                o.getDataRegistro()
        );
    }

    private AtualizacaoResponseDTO paraResponse(Atualizacao a) {
        return new AtualizacaoResponseDTO(
                a.getIdAtualizacao(),
                a.getUsuario().getNome(),
                a.getStatusAnterior(),
                a.getStatusNovo(),
                a.getDescricao(),
                a.getDataAtualizacao()
        );
    }

    private Ocorrencia buscarPorId(Integer id) {
        return ocorrenciaDAO.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Ocorrência não encontrada."));
    }

    private void validarOcorrencia(OcorrenciaRequestDTO dto) {

        if (dto == null) {
            throw new DadosInvalidosException(
                    "Os dados da ocorrência são obrigatórios."
            );
        }

        if (dto.getDescricao() == null || dto.getDescricao().isBlank()) {
            throw new DadosInvalidosException("A descrição é obrigatória.");
        }

        if (dto.getEndereco() == null || dto.getEndereco().isBlank()) {
            throw new DadosInvalidosException("O endereço é obrigatório.");
        }

        if (dto.getGravidade() == null
                || dto.getGravidade() < 1
                || dto.getGravidade() > 5) {
            throw new DadosInvalidosException(
                    "A gravidade deve estar entre 1 e 5."
            );
        }

        if (dto.getIdCategoria() == null) {
            throw new DadosInvalidosException("O ID da categoria é obrigatório.");
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