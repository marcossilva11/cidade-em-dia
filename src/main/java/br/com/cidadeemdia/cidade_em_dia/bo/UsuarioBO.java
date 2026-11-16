package br.com.cidadeemdia.cidade_em_dia.bo;

import br.com.cidadeemdia.cidade_em_dia.dao.UsuarioDAO;
import br.com.cidadeemdia.cidade_em_dia.dto.LoginRequestDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.UsuarioRequestDTO;
import br.com.cidadeemdia.cidade_em_dia.dto.UsuarioResponseDTO;
import br.com.cidadeemdia.cidade_em_dia.exception.ConflitoException;
import br.com.cidadeemdia.cidade_em_dia.exception.CredenciaisInvalidasException;
import br.com.cidadeemdia.cidade_em_dia.exception.DadosInvalidosException;
import br.com.cidadeemdia.cidade_em_dia.model.Usuario;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

@Service
public class UsuarioBO {

    private static final Pattern EMAIL =
            Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private static final Pattern TELEFONE =
            Pattern.compile("^[0-9+()\\-\\s]{8,20}$");

    private static final int MAX_NOME = 200;
    private static final int MAX_EMAIL = 200;
    private static final int MIN_SENHA = 6;
    private static final int MAX_SENHA_BYTES = 72; // limite do BCrypt

    private final UsuarioDAO usuarioDAO;
    private final PasswordEncoder passwordEncoder;

    public UsuarioBO(UsuarioDAO usuarioDAO, PasswordEncoder passwordEncoder) {
        this.usuarioDAO = usuarioDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UsuarioResponseDTO cadastrar(UsuarioRequestDTO dto) {

        validarCadastro(dto);

        String email = dto.getEmail().trim().toLowerCase();

        if (usuarioDAO.existsByEmail(email)) {
            throw new ConflitoException(
                    "Já existe um usuário cadastrado com esse e-mail."
            );
        }

        Usuario usuario = new Usuario(
                dto.getNome().trim(),
                email,
                passwordEncoder.encode(dto.getSenha()),
                dto.getTelefone().trim(),
                Usuario.TIPO_CIDADAO
        );

        return paraResponse(usuarioDAO.save(usuario));
    }

    @Transactional(readOnly = true)
    public UsuarioResponseDTO autenticar(LoginRequestDTO dto) {

        if (dto == null
                || dto.getEmail() == null || dto.getEmail().isBlank()
                || dto.getSenha() == null || dto.getSenha().isBlank()) {
            throw new DadosInvalidosException("E-mail e senha são obrigatórios.");
        }

        Usuario usuario = usuarioDAO
                .findByEmail(dto.getEmail().trim().toLowerCase())
                .orElseThrow(() ->
                        new CredenciaisInvalidasException("E-mail ou senha inválidos."));

        if (!passwordEncoder.matches(dto.getSenha(), usuario.getSenhaHash())) {
            throw new CredenciaisInvalidasException("E-mail ou senha inválidos.");
        }

        return paraResponse(usuario);
    }

    private UsuarioResponseDTO paraResponse(Usuario u) {
        return new UsuarioResponseDTO(
                u.getIdUsuario(),
                u.getNome(),
                u.getEmail(),
                u.getTelefone(),
                u.getTipo(),
                u.getDataCadastro()
        );
    }

    private void validarCadastro(UsuarioRequestDTO dto) {

        if (dto == null) {
            throw new DadosInvalidosException(
                    "Os dados do usuário são obrigatórios."
            );
        }

        if (dto.getNome() == null || dto.getNome().isBlank()) {
            throw new DadosInvalidosException("O nome é obrigatório.");
        }
        if (dto.getNome().trim().length() > MAX_NOME) {
            throw new DadosInvalidosException(
                    "O nome deve ter no máximo " + MAX_NOME + " caracteres."
            );
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            throw new DadosInvalidosException("O e-mail é obrigatório.");
        }
        String email = dto.getEmail().trim();
        if (email.length() > MAX_EMAIL || !EMAIL.matcher(email).matches()) {
            throw new DadosInvalidosException("O e-mail informado é inválido.");
        }

        if (dto.getSenha() == null || dto.getSenha().length() < MIN_SENHA) {
            throw new DadosInvalidosException(
                    "A senha deve ter no mínimo " + MIN_SENHA + " caracteres."
            );
        }
        if (dto.getSenha().getBytes(StandardCharsets.UTF_8).length > MAX_SENHA_BYTES) {
            throw new DadosInvalidosException("A senha é muito longa.");
        }

        if (dto.getTelefone() == null || dto.getTelefone().isBlank()) {
            throw new DadosInvalidosException("O telefone é obrigatório.");
        }
        if (!TELEFONE.matcher(dto.getTelefone().trim()).matches()) {
            throw new DadosInvalidosException("O telefone informado é inválido.");
        }
    }
}