package com.doranco.site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doranco.site.config.AppConfig;
import com.doranco.site.dto.UtilisateurDTO;
import com.doranco.site.dto.UtilisateurReponse;
import com.doranco.site.service.UserService;

/**
 * Contrôleur responsable de la gestion des opérations liées à l'authentification et à la gestion des utilisateurs.
 */
@RestController
@RequestMapping("/api")
public class UtilisateurController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/admin/users")
	public ResponseEntity<UtilisateurReponse> getUsers(
			@RequestParam(name = "pageNumber", defaultValue = AppConfig.NUMERO_PAGE, required = false) Integer pageNumber,
			@RequestParam(name = "pageSize", defaultValue = AppConfig.TAILLE_PAGE, required = false) Integer pageSize,
			@RequestParam(name = "sortBy", defaultValue = AppConfig.TRIER_UTILISATEURS_PAR, required = false) String sortBy,
			@RequestParam(name = "sortOrder", defaultValue = AppConfig.ORDONNER_PAR, required = false) String sortOrder) {
		
		UtilisateurReponse userResponse = userService.obtenirTousLesUtilisateurs(pageNumber, pageSize, sortBy, sortOrder);
		
		return new ResponseEntity<UtilisateurReponse>(userResponse, HttpStatus.FOUND);
	}
	
	@GetMapping("/public/users/{userId}")
	public ResponseEntity<UtilisateurDTO> getUser(@PathVariable Long userId) {
		UtilisateurDTO user = userService.obtenirUtilisateurParId(userId);
		
		return new ResponseEntity<UtilisateurDTO>(user, HttpStatus.FOUND);
	}
	
	@PutMapping("/public/users/update")
	public ResponseEntity<UtilisateurDTO> updateUser(@RequestBody UtilisateurDTO userDTO) {
	    UtilisateurDTO updatedUser = userService.mettreAJourUtilisateur(userDTO);
	    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
	}
	
	@DeleteMapping("/admin/users/{userId}")
	public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
		String status = userService.supprimerUtilisateur(userId);
		
		return new ResponseEntity<String>(status, HttpStatus.OK);
	}
}
