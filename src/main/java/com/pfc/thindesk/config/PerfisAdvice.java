package com.pfc.thindesk.config;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Expõe os perfis do usuário logado (ex.: ADMIN, TECNICO) para os templates,
 * para a sidebar e as telas mostrarem só o que cada perfil pode usar.
 * A proteção real continua no SecurityConfig; isto é só a interface.
 */
@ControllerAdvice
public class PerfisAdvice {

    @ModelAttribute("perfis")
    public Set<String> perfis(Authentication authentication) {
        if (authentication == null) {
            return Set.of();
        }
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .map(a -> a.substring("ROLE_".length()))
                .collect(Collectors.toUnmodifiableSet());
    }
}
