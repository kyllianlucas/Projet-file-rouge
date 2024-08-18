package com.doranco.site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doranco.site.config.InscriptionRequest;
import com.doranco.site.dto.AdresseDTO;
import com.doranco.site.dto.AuthRequestDTO;
import com.doranco.site.dto.JwtResponseDTO;
import com.doranco.site.dto.UtilisateurDTO;
import com.doranco.site.exception.EmailException;
import com.doranco.site.model.Utilisateur;
import com.doranco.site.service.AuthService;
import com.doranco.site.service.CaptchaService;
import com.doranco.site.service.JwtService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class AuthController {
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	private final AuthService authService;
    private final CaptchaService captchaService;


	/**
	 * Inscrit un nouvel utilisateur avec les données fournies dans la requête.
	 *
	 * @param demandeInscription les données d'inscription de l'utilisateur et de son adresse.
	 * @return une réponse HTTP avec l'objet {@link Utilisateur} inscrit et un statut 201 Created.
	 * @throws EmailException si un utilisateur avec le même email existe déjà.
	 */
    @PostMapping("/register")
    public ResponseEntity<Utilisateur> registerUser(@RequestBody InscriptionRequest request) throws EmailException {
    	String captchaToken = request.getCaptchaToken();  // Assurez-vous que le token CAPTCHA est inclus dans la requête
        if (!captchaService.verifyCaptcha(captchaToken)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }    	
    	UtilisateurDTO utilisateurDTO = request.getUtilisateurDTO();
		AdresseDTO adresseDTO = request.getAdresseDTO();
		Utilisateur registeredUser = authService.registerUser(utilisateurDTO, adresseDTO);
               
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
		 Authentication authentication = authenticationManager.authenticate(new
		UsernamePasswordAuthenticationToken(authRequestDTO.getEmail(),
		authRequestDTO.getPassword()));
		 if(authentication.isAuthenticated()){
			 return JwtResponseDTO
			 .builder()
			 .accessToken(jwtService.GenerateToken(authRequestDTO.getEmail()))
			 .build();
		 } else {
			 throw new UsernameNotFoundException("invalid user request..!!");
		 }
	}

    @PutMapping("/{userId}")
    public ResponseEntity<Utilisateur> updateUser(@PathVariable Long userId, @RequestBody Utilisateur updatedUser) {
        Utilisateur user = authService.updateUser(userId, updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
    	authService.resetPassword(email, newPassword);
        return new ResponseEntity<>("Mot de passe réinitialisé avec succès", HttpStatus.OK);
    }
}