package com.doranco.site.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doranco.site.config.AppConfig;
import com.doranco.site.dto.CommandeDTO;
import com.doranco.site.dto.CommandeReponse;
import com.doranco.site.service.CommandeService;


@RestController
@RequestMapping("/api")
public class CommandeController {
	
	@Autowired
	public CommandeService commandeService;
	
	@PostMapping("/public/utilisateurs/{emailId}/paniers/{cartId}/paiements/{paymentMethod}/commande")
	public ResponseEntity<CommandeDTO> commanderProduits(@PathVariable String emailId, @PathVariable Long cartId, @PathVariable String paymentMethod) {
		CommandeDTO commande = commandeService.passerCommande(emailId, cartId, paymentMethod);
		
		return new ResponseEntity<CommandeDTO>(commande, HttpStatus.CREATED);
	}

	@GetMapping("/admin/commandes")
	public ResponseEntity<CommandeReponse> obtenirToutesLesCommandes(
			@RequestParam(name = "numeroPage", defaultValue = AppConfig.NUMERO_PAGE, required = false) Integer numeroPage,
			@RequestParam(name = "taillePage", defaultValue = AppConfig.TAILLE_PAGE, required = false) Integer taillePage,
			@RequestParam(name = "trierPar", defaultValue = AppConfig.TRIER_COMMANDES_PAR, required = false) String trierPar,
			@RequestParam(name = "ordreTri", defaultValue = AppConfig.ORDONNER_PAR, required = false) String ordreTri) {
		
		CommandeReponse reponseCommande = commandeService.obtenirToutesLesCommandes(numeroPage, taillePage, trierPar, ordreTri);

		return new ResponseEntity<CommandeReponse>(reponseCommande, HttpStatus.FOUND);
	}
	
	@GetMapping("public/utilisateurs/{emailId}/commandes")
	public ResponseEntity<List<CommandeDTO>> obtenirCommandesParUtilisateur(@PathVariable String emailId) {
		List<CommandeDTO> commandes = commandeService.obtenirCommandesParUtilisateur(emailId);
		
		return new ResponseEntity<List<CommandeDTO>>(commandes, HttpStatus.FOUND);
	}
	
	@GetMapping("public/utilisateurs/{emailId}/commandes/{orderId}")
	public ResponseEntity<CommandeDTO> obtenirCommandeParUtilisateur(@PathVariable String emailId, @PathVariable Long orderId) {
		CommandeDTO commande = commandeService.obtenirCommande(emailId, orderId);
		
		return new ResponseEntity<CommandeDTO>(commande, HttpStatus.FOUND);
	}
	
	@PutMapping("admin/utilisateurs/{emailId}/commandes/{orderId}/statutCommande/{orderStatus}")
	public ResponseEntity<CommandeDTO> mettreAJourCommandeParUtilisateur(@PathVariable String emailId, @PathVariable Long orderId, @PathVariable String orderStatus) {
		CommandeDTO commande = commandeService.mettreÀJourCommande(emailId, orderId, orderStatus);
		
		return new ResponseEntity<CommandeDTO>(commande, HttpStatus.OK);
	}

}