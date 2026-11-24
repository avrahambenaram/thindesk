package com.pfc.thindesk.service;

import com.pfc.thindesk.entity.Usuario;
import com.pfc.thindesk.exception.RegraDeNegocioException;
import com.pfc.thindesk.repository.UsuarioRepository;
import java.util.List;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    public static final String ROLE_ADMIN = "ADMIN";
    public static final String ROLE_TECNICO = "TECNICO";
    public static final String ROLE_USUARIO = "USUARIO";

    private static final Set<String> PERFIS_VALIDOS = Set.of(ROLE_ADMIN, ROLE_TECNICO, ROLE_USUARIO);

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario criar(String username, String senha, List<String> roles) {
        String login = username.trim();
        if (usuarioRepository.existsByUsername(login)) {
            throw new RegraDeNegocioException(HttpStatus.CONFLICT, "Já existe um usuário com este login");
        }
        if (roles == null || roles.isEmpty()) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "Informe ao menos um perfil");
        }
        for (String role : roles) {
            if (!PERFIS_VALIDOS.contains(role)) {
                throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST, "Perfil inválido: " + role);
            }
        }
        Usuario usuario = new Usuario();
        usuario.setUsername(login);
        usuario.setSenha(passwordEncoder.encode(senha));
        usuario.setRoles(List.copyOf(roles));
        usuario.setAtivo(true);
        return usuarioRepository.save(usuario);
    }

    public void definirAtivo(String usuarioId, boolean ativo) {
        usuarioRepository.findById(usuarioId).ifPresent(usuario -> {
            usuario.setAtivo(ativo);
            usuarioRepository.save(usuario);
        });
    }

    public void deletar(String usuarioId) {
        usuarioRepository.deleteById(usuarioId);
    }
}
