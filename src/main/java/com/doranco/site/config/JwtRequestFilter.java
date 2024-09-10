package com.doranco.site.config;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.doranco.site.service.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    @Qualifier("userInfoService")
    private UserDetailsService userDetailsService;
    
    private RequestAttributeSecurityContextRepository repository = new RequestAttributeSecurityContextRepository();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtService.extractUsername(jwt);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtService.validateToken(jwt, userDetails)) {
            	 Claims claims = jwtService.extractAllClaims(jwt);
            	 boolean isAdmin = claims.get("isAdmin", Boolean.class); // Extraire le flag isAdmin

                 // Créer la liste des autorités en fonction du rôle
                 List<GrantedAuthority> authorities;
                 if (isAdmin) {
                     authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
                 } else {
                     authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
                 }

                 // Créer l'authentification avec les autorités (rôle)
                 UsernamePasswordAuthenticationToken authenticationToken =
                         new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                 authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                 // Définir le contexte de sécurité
                 SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                
            }
        }
        filterChain.doFilter(request, response);
    }
}
