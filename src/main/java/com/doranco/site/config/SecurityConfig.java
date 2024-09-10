package com.doranco.site.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.doranco.site.service.UserInfoService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserInfoService userDetail;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utilise BCrypt pour encoder les mots de passe
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Désactiver CSRF pour une API stateless
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/users/register", "/api/users/login").permitAll() // Ces endpoints sont accessibles sans authentification
                .requestMatchers("/api/users/update").hasAnyRole("USER", "ADMIN") // Accessible par USER et ADMIN
                .requestMatchers("/api/users/articles/**").hasRole("USER") // Accessible uniquement par USER
                .requestMatchers("/api/users/articles/admin/**").hasAuthority("ROLE_ADMIN") // Accessible uniquement par ADMIN
                .anyRequest().authenticated() // Toute autre requête doit être authentifiée
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Gérer les sessions en mode stateless
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class) // Ajouter le filtre JWT avant le filtre d'authentification
            .formLogin(AbstractHttpConfigurer::disable) // Désactiver l'authentification par formulaire
            .logout(logout -> logout
                .logoutUrl("/api/users/logout")
                .logoutSuccessHandler(logoutSuccessHandler())
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            );

        return http.build();
    }

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().flush();
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetail);
        provider.setPasswordEncoder(passwordEncoder()); // Utilise BCrypt pour l'encodage des mots de passe
        return provider;
    }
}
