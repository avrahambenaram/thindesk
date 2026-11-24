package com.pfc.thindesk.controller;

import com.pfc.thindesk.dto.CriarUsuarioRequest;
import com.pfc.thindesk.entity.Usuario;
import com.pfc.thindesk.exception.RegraDeNegocioException;
import com.pfc.thindesk.service.UsuarioService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Gerencia logins de usuários da empresa (perfil USUARIO) e de administradores (ADMIN).
 * Logins de técnicos são criados pelo cadastro de técnicos, para manter o vínculo.
 * Acesso restrito a ADMIN (ver SecurityConfig).
 */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {

    private final UsuarioService usuarioService;

    public UsuarioRestController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listarTodos();
    }

    @PostMapping
    public Usuario criar(@Valid @RequestBody CriarUsuarioRequest request) {
        if (request.roles().contains(UsuarioService.ROLE_TECNICO)) {
            throw new RegraDeNegocioException(HttpStatus.BAD_REQUEST,
                    "Logins de técnicos são criados pelo cadastro de técnicos");
        }
        return usuarioService.criar(request.username(), request.senha(), request.roles());
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable("id") String id) {
        usuarioService.deletar(id);
    }
}
