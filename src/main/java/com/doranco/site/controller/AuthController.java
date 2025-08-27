package com.doranco.site.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doranco.site.dto.IdentifiantsConnexion;
import com.doranco.site.dto.UtilisateurDTO;
import com.doranco.site.securite.JWTUtil;
import com.doranco.site.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api")
public class AuthController{

	@Autowired
	private UserService serviceUtilisateur;

	@Autowired
	private JWTUtil jwtUtil;

	@Autowired
	private AuthenticationManager gestionnaireAuthentification;

	@Autowired
	private PasswordEncoder encodeurMotDePasse;

	@PostMapping("/inscription")
    public ResponseEntity<Map<String, Object>> gestionInscription(@Valid @RequestBody UtilisateurDTO utilisateurDTO) {
        log.info("Tentative d’inscription pour l’email: {}", utilisateurDTO.getEmail());

        try {
            String motDePasseEncode = encodeurMotDePasse.encode(utilisateurDTO.getMotDePasse());
            utilisateurDTO.setMotDePasse(motDePasseEncode);

            UtilisateurDTO utilisateurEnregistre = serviceUtilisateur.enregistrerUtilisateur(utilisateurDTO);

            String role = utilisateurEnregistre.getRoles().stream()
                    .findFirst()
                    .orElse("USER");

            String token = jwtUtil.generateToken(utilisateurEnregistre.getEmail(), role);

            log.info("Inscription réussie pour l’email: {}, rôle attribué: {}", utilisateurEnregistre.getEmail(), role);

            return new ResponseEntity<>(Collections.singletonMap("jwt-token", token), HttpStatus.CREATED);

        } catch (Exception e) {
            log.error("Erreur lors de l’inscription pour l’email: {} - {}", utilisateurDTO.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Échec de l’inscription: " + e.getMessage()));
        }
    }

    @PostMapping("/connexion")
    public ResponseEntity<Map<String, Object>> gestionConnexion(@Valid @RequestBody IdentifiantsConnexion identifiants) {
        log.info("Tentative de connexion pour l’email: {}", identifiants.getEmail());

        try {
            UsernamePasswordAuthenticationToken authCredentials =
                    new UsernamePasswordAuthenticationToken(identifiants.getEmail(), identifiants.getMotDePasse());

            Authentication authentication = gestionnaireAuthentification.authenticate(authCredentials);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            String role = userDetails.getAuthorities().stream()
                    .map(grantedAuthority -> grantedAuthority.getAuthority())
                    .findFirst()
                    .orElse("USER");

            if (!role.equals("ADMIN") && !role.equals("USER")) {
                log.warn("Connexion avec rôle inattendu: {} pour l’email: {}", role, identifiants.getEmail());
                throw new IllegalStateException("Rôle invalide: " + role);
            }

            String token = jwtUtil.generateToken(identifiants.getEmail(), role);

            log.info("Connexion réussie pour l’email: {}, rôle: {}", identifiants.getEmail(), role);

            return ResponseEntity.ok(Collections.singletonMap("jwt-token", token));

        } catch (BadCredentialsException e) {
            log.warn("Échec de connexion pour l’email: {} - mauvais identifiants", identifiants.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Identifiants incorrects"));
        } catch (Exception e) {
            log.error("Erreur inattendue lors de la connexion pour l’email: {} - {}", identifiants.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Erreur serveur: " + e.getMessage()));
        }
    }

}
