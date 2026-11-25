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
     * Perfis:
     *  ADMIN   - cadastros (técnicos, clientes, recursos, horários, usuários, tipos de chamado)
     *  TECNICO - vê chamados e tipos de chamado; resolve chamados
     *  USUARIO - abre chamados e vê os chamados
     * A primeira regra que casa com a requisição é a que vale.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Público: login, estáticos e documentação
                .requestMatchers("/login", "/css/**", "/dist/**", "/plugins/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()

                // Histórico completo do chamado: somente admin
                .requestMatchers("/chamados/*/detalhes").hasRole("ADMIN")

                // Leitura de chamados e tipos de chamado: qualquer usuário logado (inclusive técnico)
                .requestMatchers(HttpMethod.GET, "/api/chamados", "/api/chamados/**",
                        "/api/tipos-chamado", "/api/tipos-chamado/**").authenticated()
                .requestMatchers("/chamados", "/chamados/**", "/tipo-chamado").authenticated()

                // Abrir chamado: usuários da empresa e admin (técnico não abre)
                .requestMatchers(HttpMethod.POST, "/api/chamados").hasAnyRole("USUARIO", "ADMIN")
                .requestMatchers("/abrir-chamado").hasAnyRole("USUARIO", "ADMIN")

                // Resolver chamado: somente técnico
                .requestMatchers(HttpMethod.PUT, "/api/chamados/*/resolver").hasRole("TECNICO")

                // Tipos de chamado: criar/editar/excluir somente admin
                .requestMatchers("/api/tipos-chamado", "/api/tipos-chamado/**").hasRole("ADMIN")

                // Cadastros e administração: somente admin (telas e API)
                .requestMatchers("/tecnicos", "/tecnicos/**", "/api/tecnicos", "/api/tecnicos/**").hasRole("ADMIN")
                .requestMatchers("/clientes", "/clientes/**", "/api/clientes", "/api/clientes/**").hasRole("ADMIN")
                .requestMatchers("/recursos", "/recursos/**", "/api/recursos", "/api/recursos/**").hasRole("ADMIN")
                .requestMatchers("/ajustes-horarios", "/ajustes-horarios/**",
                        "/api/ajustes-horarios", "/api/ajustes-horarios/**").hasRole("ADMIN")
                .requestMatchers("/api/usuarios", "/api/usuarios/**").hasRole("ADMIN")

                // Demais rotas: exigem login
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
