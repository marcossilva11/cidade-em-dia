package br.com.cidadeemdia.cidade_em_dia.bo;

import br.com.cidadeemdia.cidade_em_dia.dao.BairroDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.CategoriaDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.PrioridadeDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.StatusDAO;
import br.com.cidadeemdia.cidade_em_dia.dto.BairroResponseDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.CategoriaResponseDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.OpcaoResponseDTO;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ApoioBO {

    private final CategoriaDAO categoriaDAO;
    private final BairroDAO bairroDAO;
    private final StatusDAO statusDAO;
    private final PrioridadeDAO prioridadeDAO;

    public ApoioBO(
            CategoriaDAO categoriaDAO,
            BairroDAO bairroDAO,
            StatusDAO statusDAO,
            PrioridadeDAO prioridadeDAO) {

        this.categoriaDAO = categoriaDAO;
        this.bairroDAO = bairroDAO;
        this.statusDAO = statusDAO;
        this.prioridadeDAO = prioridadeDAO;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaDAO.findAll(Sort.by("nome"))
                .stream()
                .map(c -> new CategoriaResponseDTO(
                        c.getIdCategoria(), c.getNome(), c.getDescricao()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<BairroResponseDTO> listarBairros() {
        return bairroDAO.findAll(Sort.by("nome"))
                .stream()
                .map(b -> new BairroResponseDTO(
                        b.getIdBairro(), b.getNome(), b.getCidade(), b.getEstado()))
                .toList();
    }

    // Status e prioridades ordenados pelo id: a ordem do cadastro é a ordem lógica
    @Transactional(readOnly = true)
    public List<OpcaoResponseDTO> listarStatus() {
        return statusDAO.findAll(Sort.by("idStatus"))
                .stream()
                .map(s -> new OpcaoResponseDTO(
                        s.getIdStatus(), s.getNome(), s.getDescricao()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OpcaoResponseDTO> listarPrioridades() {
        return prioridadeDAO.findAll(Sort.by("idPrioridade"))
                .stream()
                .map(p -> new OpcaoResponseDTO(
                        p.getIdPrioridade(), p.getNome(), p.getDescricao()))
                .toList();
    }
}