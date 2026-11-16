package br.com.cidadeemdia.cidade_em_dia.bo;

import br.com.cidadeemdia.cidade_em_dia.dao.BairroDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.CategoriaDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.HistoricoOcorrenciaDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.OcorrenciaDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.PrioridadeDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.RelatoDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.StatusDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.UsuarioDAO;

import br.com.cidadeemdia.cidade_em_dia.dto.HistoricoResponseDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.OcorrenciaRequestDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.OcorrenciaResponseDTO;

import br.com.cidadeemdia.cidade_em_dia.exception.AcessoNegadoException;
import br.com.cidadeemdia.cidade_em_dia.exception.ConflitoException;
import br.com.cidadeemdia.cidade_em_dia.exception.DadosInvalidosException;
import br.com.cidadeemdia.cidade_em_dia.exception.RecursoNaoEncontradoException;

import br.com.cidadeemdia.cidade_em_dia.model.Bairro;
import br.com.cidadeemdia.cidade_em_dia.model.Categoria;
import br.com.cidadeemdia.cidade_em_dia.model.HistoricoOcorrencia;
import br.com.cidadeemdia.cidade_em_dia.model.Ocorrencia;
import br.com.cidadeemdia.cidade_em_dia.model.Prioridade;
import br.com.cidadeemdia.cidade_em_dia.model.Status;
import br.com.cidadeemdia.cidade_em_dia.model.Usuario;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OcorrenciaBO {

    private static final String STATUS_INICIAL = "PENDENTE";
    private static final String PRIORIDADE_INICIAL = "BAIXA";

    private static final int MAX_TITULO = 100;
    private static final int MAX_DESCRICAO = 500;
    private static final int MAX_ENDERECO = 200;
    private static final int MAX_FOTO = 500;
    private static final int MAX_OBSERVACAO = 500;

    private final OcorrenciaDAO ocorrenciaDAO;
    private final RelatoDAO relatoDAO;
    private final HistoricoOcorrenciaDAO historicoDAO;
    private final UsuarioDAO usuarioDAO;
    private final CategoriaDAO categoriaDAO;
    private final BairroDAO bairroDAO;
    private final StatusDAO statusDAO;
    private final PrioridadeDAO prioridadeDAO;

    public OcorrenciaBO(
            OcorrenciaDAO ocorrenciaDAO,
            RelatoDAO relatoDAO,
            HistoricoOcorrenciaDAO historicoDAO,
            UsuarioDAO usuarioDAO,
            CategoriaDAO categoriaDAO,
            BairroDAO bairroDAO,
            StatusDAO statusDAO,
            PrioridadeDAO prioridadeDAO) {

        this.ocorrenciaDAO = ocorrenciaDAO;
        this.relatoDAO = relatoDAO;
        this.historicoDAO = historicoDAO;
        this.usuarioDAO = usuarioDAO;
        this.categoriaDAO = categoriaDAO;
        this.bairroDAO = bairroDAO;
        this.statusDAO = statusDAO;
        this.prioridadeDAO = prioridadeDAO;
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

        Usuario usuario = buscarUsuario(dto.getIdUsuario());
        Bairro bairro = buscarBairro(dto.getIdBairro());
        Categoria categoria = buscarCategoria(dto.getIdCategoria());

        Status statusInicial = statusDAO.findByNome(STATUS_INICIAL)
                .orElseThrow(() -> new IllegalStateException(
                        "Status inicial '" + STATUS_INICIAL
                                + "' não cadastrado no banco."));

        Prioridade prioridadeInicial = prioridadeDAO.findByNome(PRIORIDADE_INICIAL)
                .orElseThrow(() -> new IllegalStateException(
                        "Prioridade inicial '" + PRIORIDADE_INICIAL
                                + "' não cadastrada no banco."));

        Ocorrencia ocorrencia = new Ocorrencia(
                usuario,
                bairro,
                categoria,
                prioridadeInicial,
                statusInicial,
                dto.getTitulo().trim(),
                dto.getDescricao().trim(),
                limparOpcional(dto.getFoto()),
                dto.getEndereco().trim(),
                dto.getLatitude(),
                dto.getLongitude()
        );

        Ocorrencia salva = ocorrenciaDAO.save(ocorrencia);

        historicoDAO.save(new HistoricoOcorrencia(
                salva,
                usuario,
                statusInicial,
                "Ocorrência registrada."
        ));

        return paraResponse(salva);
    }

    @Transactional
    public OcorrenciaResponseDTO atualizar(Integer id, OcorrenciaRequestDTO dto) {

        validarOcorrencia(dto);

        Ocorrencia ocorrencia = buscarPorId(id);
        Bairro bairro = buscarBairro(dto.getIdBairro());
        Categoria categoria = buscarCategoria(dto.getIdCategoria());

        ocorrencia.atualizarDados(
                bairro,
                categoria,
                dto.getTitulo().trim(),
                dto.getDescricao().trim(),
                limparOpcional(dto.getFoto()),
                dto.getEndereco().trim(),
                dto.getLatitude(),
                dto.getLongitude()
        );

        return paraResponse(ocorrenciaDAO.save(ocorrencia));
    }

    @Transactional
    public void excluir(Integer id) {

        Ocorrencia ocorrencia = buscarPorId(id);

        long quantidadeRelatos =
                relatoDAO.countByOcorrencia_IdOcorrencia(id);

        List<HistoricoOcorrencia> historico =
                historicoDAO
                        .findByOcorrencia_IdOcorrenciaOrderByDataAlteracaoDescIdHistoricoDesc(id);

        if (quantidadeRelatos > 0 || historico.size() > 1) {
            throw new ConflitoException(
                    "Não é possível excluir uma ocorrência que já possui relatos ou mudanças de status."
            );
        }

        historicoDAO.deleteAll(historico);
        historicoDAO.flush();
        ocorrenciaDAO.delete(ocorrencia);
    }

    @Transactional
    public OcorrenciaResponseDTO atualizarStatus(
            Integer id,
            String nomeNovoStatus,
            Integer idUsuario,
            String descricao) {

        if (nomeNovoStatus == null || nomeNovoStatus.isBlank()) {
            throw new DadosInvalidosException("O novo status é obrigatório.");
        }

        Usuario usuario = buscarUsuario(idUsuario);

        if (!usuario.isAdmin()) {
            throw new AcessoNegadoException(
                    "Apenas administradores podem alterar o status de uma ocorrência."
            );
        }

        Ocorrencia ocorrencia = buscarPorId(id);

        Status novoStatus = statusDAO
                .findByNome(nomeNovoStatus.trim().toUpperCase())
                .orElseThrow(() -> new DadosInvalidosException(
                        "Status inválido: " + nomeNovoStatus.trim() + "."));

        if (ocorrencia.getStatus().getIdStatus().equals(novoStatus.getIdStatus())) {
            throw new DadosInvalidosException(
                    "A ocorrência já possui esse status."
            );
        }

        // OBSERVACAO_HISTORICO é obrigatória no banco
        String observacao = (descricao == null || descricao.isBlank())
                ? "Status alterado para " + novoStatus.getNome() + "."
                : descricao.trim();

        validarTamanho(observacao, MAX_OBSERVACAO, "A observação");

        ocorrencia.alterarStatus(novoStatus);

        ocorrenciaDAO.save(ocorrencia);
        historicoDAO.save(new HistoricoOcorrencia(
                ocorrencia,
                usuario,
                novoStatus,
                observacao
        ));

        return paraResponse(ocorrencia);
    }

    @Transactional(readOnly = true)
    public List<HistoricoResponseDTO> listarHistorico(Integer id) {

        buscarPorId(id);

        return historicoDAO
                .findByOcorrencia_IdOcorrenciaOrderByDataAlteracaoDescIdHistoricoDesc(id)
                .stream()
                .map(this::paraResponse)
                .toList();
    }

    private OcorrenciaResponseDTO paraResponse(Ocorrencia o) {
        return new OcorrenciaResponseDTO(
                o.getIdOcorrencia(),
                o.getTitulo(),
                o.getUsuario().getNome(),
                o.getBairro().getNome(),
                o.getCategoria().getNome(),
                o.getDescricao(),
                o.getFoto(),
                o.getEndereco(),
                o.getLatitude(),
                o.getLongitude(),
                o.getStatus().getNome(),
                o.getPrioridade().getNome(),
                o.getDataRegistro()
        );
    }

    private HistoricoResponseDTO paraResponse(HistoricoOcorrencia h) {
        return new HistoricoResponseDTO(
                h.getIdHistorico(),
                h.getUsuario().getNome(),
                h.getStatus().getNome(),
                h.getObservacao(),
                h.getDataAlteracao()
        );
    }

    private Ocorrencia buscarPorId(Integer id) {
        return ocorrenciaDAO.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Ocorrência não encontrada."));
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

    private Bairro buscarBairro(Integer id) {

        if (id == null) {
            throw new DadosInvalidosException("O ID do bairro é obrigatório.");
        }

        return bairroDAO.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Bairro não encontrado."));
    }

    private void validarOcorrencia(OcorrenciaRequestDTO dto) {

        if (dto == null) {
            throw new DadosInvalidosException(
                    "Os dados da ocorrência são obrigatórios."
            );
        }

        if (dto.getTitulo() == null || dto.getTitulo().isBlank()) {
            throw new DadosInvalidosException("O título é obrigatório.");
        }
        validarTamanho(dto.getTitulo().trim(), MAX_TITULO, "O título");

        if (dto.getDescricao() == null || dto.getDescricao().isBlank()) {
            throw new DadosInvalidosException("A descrição é obrigatória.");
        }
        validarTamanho(dto.getDescricao().trim(), MAX_DESCRICAO, "A descrição");

        if (dto.getEndereco() == null || dto.getEndereco().isBlank()) {
            throw new DadosInvalidosException("O endereço é obrigatório.");
        }
        validarTamanho(dto.getEndereco().trim(), MAX_ENDERECO, "O endereço");

        if (dto.getFoto() != null) {
            validarTamanho(dto.getFoto().trim(), MAX_FOTO, "O link da foto");
        }

        if (dto.getLatitude() != null
                && dto.getLatitude().abs().compareTo(new BigDecimal("90")) > 0) {
            throw new DadosInvalidosException(
                    "A latitude deve estar entre -90 e 90."
            );
        }

        if (dto.getLongitude() != null
                && dto.getLongitude().abs().compareTo(new BigDecimal("180")) > 0) {
            throw new DadosInvalidosException(
                    "A longitude deve estar entre -180 e 180."
            );
        }
    }

    private void validarTamanho(String valor, int maximo, String campo) {
        if (valor.length() > maximo) {
            throw new DadosInvalidosException(
                    campo + " deve ter no máximo " + maximo + " caracteres."
            );
        }
    }

    private String limparOpcional(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }
}