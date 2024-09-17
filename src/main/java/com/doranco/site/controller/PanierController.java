package com.doranco.site.controller;

import java.util.List;

import com.doranco.site.dto.PanierDTO;
import com.doranco.site.service.PanierService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")
public class PanierController {
	
	@Autowired
	private PanierService servicePanier;

	@PostMapping("/public/paniers/{panierId}/produits/{produitId}/quantite/{quantite}")
	public ResponseEntity<PanierDTO> ajouterProduitAuPanier(@PathVariable Long panierId, @PathVariable Long produitId, @PathVariable Integer quantite) {
		PanierDTO panierDTO = servicePanier.ajouterArticleAuPanier(panierId, produitId, quantite);
		
		return new ResponseEntity<PanierDTO>(panierDTO, HttpStatus.CREATED);
	}
	
	@GetMapping("/admin/paniers")
	public ResponseEntity<List<PanierDTO>> obtenirPaniers() {
		
		List<PanierDTO> paniersDTO = servicePanier.obtenirTousLesPaniers();
		
		return new ResponseEntity<List<PanierDTO>>(paniersDTO, HttpStatus.FOUND);
	}
	
	@GetMapping("/public/utilisateurs/{emailId}/paniers/{panierId}")
	public ResponseEntity<PanierDTO> obtenirPanierParId(@PathVariable String emailId, @PathVariable Long panierId) {
		PanierDTO panierDTO = servicePanier.obtenirPanier(emailId, panierId);
		
		return new ResponseEntity<PanierDTO>(panierDTO, HttpStatus.FOUND);
	}
	
	@PutMapping("/public/paniers/{panierId}/produits/{produitId}/quantite/{quantite}")
	public ResponseEntity<PanierDTO> mettreAJourProduitPanier(@PathVariable Long panierId, @PathVariable Long produitId, @PathVariable Integer quantite) {
		PanierDTO panierDTO = servicePanier.mettreÀJourQuantitéArticleDansPanier(panierId, produitId, quantite);
		
		return new ResponseEntity<PanierDTO>(panierDTO, HttpStatus.OK);
	}
	
	@DeleteMapping("/public/paniers/{panierId}/produit/{produitId}")
	public ResponseEntity<String> supprimerProduitDuPanier(@PathVariable Long panierId, @PathVariable Long produitId) {
		String statut = servicePanier.supprimerArticleDuPanier(panierId, produitId);
		
		return new ResponseEntity<String>(statut, HttpStatus.OK);
	}
}