package com.doranco.site.controller;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

@RestController
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
	public ResponseEntity<Map<String, Object>> gestionInscription(@Valid @RequestBody UtilisateurDTO utilisateurDTO) throws UsernameNotFoundException {
		String motDePasseEncode = encodeurMotDePasse.encode(utilisateurDTO.getMotDePasse());
	    utilisateurDTO.setMotDePasse(motDePasseEncode);

	    UtilisateurDTO utilisateurEnregistre = serviceUtilisateur.enregistrerUtilisateur(utilisateurDTO);

	    // Extraire le rôle (ici on suppose qu'il y a un seul rôle pour simplifier)
	    String role = utilisateurEnregistre.getRoles().stream().findFirst().orElse("USER"); // ou un autre rôle par défaut

	    String token = jwtUtil.generateToken(utilisateurEnregistre.getEmail(), role);

	    return new ResponseEntity<Map<String, Object>>(Collections.singletonMap("jwt-token", token), HttpStatus.CREATED);
	}


	@PostMapping("/connexion")
	public Map<String, Object> gestionConnexion(@Valid @RequestBody IdentifiantsConnexion identifiants) {

	    // Créer l'objet d'authentification
	    UsernamePasswordAuthenticationToken authCredentials = new UsernamePasswordAuthenticationToken(
	            identifiants.getEmail(), identifiants.getMotDePasse());

	    // Authentifier l'utilisateur
	    Authentication authentication = gestionnaireAuthentification.authenticate(authCredentials);

	    // Mettre à jour le contexte de sécurité avec l'authentification
	    SecurityContextHolder.getContext().setAuthentication(authentication);

	    // Récupérer les détails de l'utilisateur authentifié
	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

	    // Extraire le rôle de l'utilisateur
	    String role = userDetails.getAuthorities().stream()
	            .map(grantedAuthority -> grantedAuthority.getAuthority())
	            .findFirst()
	            .orElse("USER"); // Valeur par défaut si aucun rôle trouvé

	    // Vérifier si le rôle est valide (ADMIN ou USER)
	    if (!role.equals("ADMIN") && !role.equals("USER")) {
	        throw new IllegalStateException("Rôle invalide: " + role);
	    }
	    
	    // Générer le token avec le rôle inclus
	    String token = jwtUtil.generateToken(identifiants.getEmail(), role);

	    // Retourner le token JWT
	    return Collections.singletonMap("jwt-token", token);
	}

}
