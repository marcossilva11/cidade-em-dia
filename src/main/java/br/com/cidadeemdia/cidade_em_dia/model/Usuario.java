package br.com.cidadeemdia.cidade_em_dia.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "USUARIO")
public class Usuario {

    public static final String TIPO_CIDADAO = "CIDADAO";
    public static final String TIPO_ADMIN = "ADMIN";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(
            name = "seq_usuario",
            sequenceName = "SEQ_USUARIO",
            allocationSize = 1
    )
    @Column(name = "ID_USUARIO")
    private Integer idUsuario;

    @Column(name = "NOME_USUARIO", nullable = false, length = 200)
    private String nome;

    @Column(name = "EMAIL_USUARIO", nullable = false, unique = true, length = 200)
    private String email;

    @Column(name = "SENHA_USUARIO", nullable = false, length = 255)
    private String senhaHash;

    @Column(name = "TELEFONE_USUARIO", nullable = false, length = 20)
    private String telefone;

    @Column(name = "TIPO_USUARIO", nullable = false, length = 20)
    private String tipo;

    @Column(name = "DT_CADASTRO_USUARIO", nullable = false)
    private LocalDateTime dataCadastro;

    protected Usuario() {
    }

    public Usuario(String nome, String email, String senhaHash,
                   String telefone, String tipo) {
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.telefone = telefone;
        this.tipo = (tipo == null || tipo.isBlank()) ? TIPO_CIDADAO : tipo;
        this.dataCadastro = LocalDateTime.now();
    }

    public void atualizarDados(String nome, String email, String telefone) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
    }

    public void alterarSenha(String novaSenhaHash) {
        this.senhaHash = novaSenhaHash;
    }

    public boolean isAdmin() {
        return TIPO_ADMIN.equalsIgnoreCase(tipo);
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getTipo() {
        return tipo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
}