package com.pfc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /*
     * Perfis: ADMIN (cadastros), TECNICO (resolve chamados) e USUARIO (abre chamados).
     * A ordem importa: a primeira regra que casa com a requisição é a que vale.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Público: login, estáticos e documentação
                .requestMatchers("/login", "/css/**", "/dist/**", "/plugins/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // Leitura de cadastros e chamados: qualquer usuário logado
                .requestMatchers(HttpMethod.GET, "/api/tecnicos", "/api/tecnicos/**",
                        "/api/tipos-chamado", "/api/tipos-chamado/**",
                        "/api/recursos", "/api/recursos/**",
                        "/api/chamados", "/api/chamados/**",
                        "/api/clientes", "/api/clientes/**").authenticated()

                // Abrir chamado: qualquer usuário logado
                .requestMatchers(HttpMethod.POST, "/api/chamados").authenticated()

                // Resolver chamado: somente técnicos
                .requestMatchers(HttpMethod.PUT, "/api/chamados/*/resolver").hasRole("TECNICO")

                // Cadastros e administração: somente ADMIN
                .requestMatchers("/api/usuarios", "/api/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/api/clientes", "/api/clientes/**").hasRole("ADMIN")
                .requestMatchers("/api/tecnicos", "/api/tecnicos/**",
                        "/api/tipos-chamado", "/api/tipos-chamado/**",
                        "/api/recursos", "/api/recursos/**",
                        "/api/ajustes-horarios", "/api/ajustes-horarios/**").hasRole("ADMIN")
                .requestMatchers("/ajustes-horarios/salvar", "/ajustes-horarios/deletar/**").hasRole("ADMIN")

                // Demais rotas (telas e API): exigem login
                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout")
            );
        return http.build();
    }
}
