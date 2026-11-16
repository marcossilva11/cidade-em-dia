package br.com.cidadeemdia.cidade_em_dia.bo;

import br.com.cidadeemdia.cidade_em_dia.dao.OcorrenciaDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.PrioridadeDAO;
import br.com.cidadeemdia.cidade_em_dia.dao.RelatoDAO;
import br.com.cidadeemdia.cidade_em_dia.model.Categoria;
import br.com.cidadeemdia.cidade_em_dia.model.Ocorrencia;
import br.com.cidadeemdia.cidade_em_dia.model.Prioridade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 *
 ---------- Regra de prioridade automatica das ocorrencias ----------

 PONTOS = (relatos x 10) + (dias em aberto x 5) + peso da categoria

    0 a 14  -> BAIXA
   15 a 29  -> MEDIA
   30 a 49  -> ALTA
   50+      -> CRITICA

  - Relatos: quantas pessoas confirmaram a ocorrencia
  - Dias em aberto: dias desde o registro
  - Peso da categoria: impacto tipico do problema (por exemplo: alagamento pesa mais que lixo)

 Ocorrencias resolvidas mantem a ultima prioridade calculada

 **/

@Service
public class PrioridadeBO {

    private static final String STATUS_RESOLVIDO = "RESOLVIDO";

    private static final int PONTOS_POR_RELATO = 10;
    private static final int PONTOS_POR_DIA = 5;

    private static final int LIMITE_MEDIA = 15;
    private static final int LIMITE_ALTA = 30;
    private static final int LIMITE_CRITICA = 50;

    private static final Map<String, Integer> PESO_CATEGORIA = Map.of(
            "alagamento", 20,
            "buraco na via", 10,
            "iluminacao publica", 10,
            "sinalizacao", 10,
            "calcada danificada", 5,
            "lixo e entulho", 5
    );

    private final PrioridadeDAO prioridadeDAO;
    private final OcorrenciaDAO ocorrenciaDAO;
    private final RelatoDAO relatoDAO;

    public PrioridadeBO(
            PrioridadeDAO prioridadeDAO,
            OcorrenciaDAO ocorrenciaDAO,
            RelatoDAO relatoDAO) {

        this.prioridadeDAO = prioridadeDAO;
        this.ocorrenciaDAO = ocorrenciaDAO;
        this.relatoDAO = relatoDAO;
    }

    public static int calcularPontos(long relatos, long diasEmAberto, String nomeCategoria) {

        int peso = nomeCategoria == null
                ? 0
                : PESO_CATEGORIA.getOrDefault(nomeCategoria.trim().toLowerCase(), 0);

        return (int) (relatos * PONTOS_POR_RELATO
                + diasEmAberto * PONTOS_POR_DIA
                + peso);
    }

    public static String nivelPara(int pontos) {

        if (pontos >= LIMITE_CRITICA) {
            return "CRITICA";
        }
        if (pontos >= LIMITE_ALTA) {
            return "ALTA";
        }
        if (pontos >= LIMITE_MEDIA) {
            return "MEDIA";
        }
        return "BAIXA";
    }

    @Transactional(readOnly = true)
    public Prioridade prioridadeInicial(Categoria categoria) {
        return buscarPrioridade(nivelPara(calcularPontos(0, 0, categoria.getNome())));
    }

    @Transactional
    public boolean reavaliar(Ocorrencia ocorrencia) {

        if (STATUS_RESOLVIDO.equals(ocorrencia.getStatus().getNome())) {
            return false;
        }

        long relatos = relatoDAO.countByOcorrencia_IdOcorrencia(
                ocorrencia.getIdOcorrencia());

        long dias = Math.max(0, ChronoUnit.DAYS.between(
                ocorrencia.getDataRegistro(), LocalDateTime.now()));

        int pontos = calcularPontos(
                relatos, dias, ocorrencia.getCategoria().getNome());

        Prioridade nova = buscarPrioridade(nivelPara(pontos));

        if (nova.getIdPrioridade().equals(ocorrencia.getPrioridade().getIdPrioridade())) {
            return false;
        }

        ocorrencia.alterarPrioridade(nova);
        ocorrenciaDAO.save(ocorrencia);
        return true;
    }

    @Transactional
    public int recalcularTodas() {

        int alteradas = 0;

        for (Ocorrencia ocorrencia : ocorrenciaDAO.findAllByOrderByDataRegistroDesc()) {
            if (reavaliar(ocorrencia)) {
                alteradas++;
            }
        }

        return alteradas;
    }

    private Prioridade buscarPrioridade(String nome) {
        return prioridadeDAO.findByNome(nome)
                .orElseThrow(() -> new IllegalStateException(
                        "Prioridade '" + nome + "' não cadastrada no banco."));
    }
}