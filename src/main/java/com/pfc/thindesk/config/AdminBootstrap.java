package com.pfc.thindesk.config;

import com.pfc.thindesk.repository.UsuarioRepository;
import com.pfc.thindesk.service.UsuarioService;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Cria o administrador inicial a partir de ADMIN_USERNAME / ADMIN_PASSWORD (.env),
 * caso ainda não exista. Não altera a senha de um admin já cadastrado.
 */
@Component
public class AdminBootstrap implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioService usuarioService;
    private final String adminUsername;
    private final String adminPassword;

    public AdminBootstrap(UsuarioRepository usuarioRepository,
                          UsuarioService usuarioService,
                          @Value("${ADMIN_USERNAME}") String adminUsername,
                          @Value("${ADMIN_PASSWORD}") String adminPassword) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService = usuarioService;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!usuarioRepository.existsByUsername(adminUsername)) {
            usuarioService.criar(adminUsername, adminPassword, List.of(UsuarioService.ROLE_ADMIN));
        }
    }
}
