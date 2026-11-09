package br.com.cidadeemdia.cidade_em_dia.exception;

public class DadosInvalidosException extends RuntimeException {

    public DadosInvalidosException(String mensagem) {
        super(mensagem);
    }
}
