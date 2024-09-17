package com.doranco.site.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doranco.site.dto.AdresseDTO;
import com.doranco.site.model.Adresse;
import com.doranco.site.service.AdresseService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/admin")
public class AdresseController {
	
	@Autowired
	private AdresseService serviceAdresse;
	
	@PostMapping("/adresse")
	public ResponseEntity<AdresseDTO> creerAdresse(@Valid @RequestBody AdresseDTO adresseDTO) {
		AdresseDTO adresseEnregistree = serviceAdresse.créerAdresse(adresseDTO);
		
		return new ResponseEntity<AdresseDTO>(adresseEnregistree, HttpStatus.CREATED);
	}
	
	@GetMapping("/adresses")
	public ResponseEntity<List<AdresseDTO>> obtenirAdresses() {
		List<AdresseDTO> adresses = serviceAdresse.obtenirAdresses();
		
		return new ResponseEntity<List<AdresseDTO>>(adresses, HttpStatus.FOUND);
	}
	
	@GetMapping("/adresses/{adresseId}")
	public ResponseEntity<AdresseDTO> obtenirAdresse(@PathVariable Long adresseId) {
		AdresseDTO adresse = serviceAdresse.obtenirAdresse(adresseId);
		
		return new ResponseEntity<AdresseDTO>(adresse, HttpStatus.FOUND);
	}
	
	@PutMapping("/adresses/{adresseId}")
	public ResponseEntity<AdresseDTO> mettreAJourAdresse(@PathVariable Long adresseId, @RequestBody Adresse adresse) {
		AdresseDTO adresseMiseAJour = serviceAdresse.mettreÀJourAdresse(adresseId, adresse);
		
		return new ResponseEntity<AdresseDTO>(adresseMiseAJour, HttpStatus.OK);
	}
	
	@DeleteMapping("/adresses/{adresseId}")
	public ResponseEntity<String> supprimerAdresse(@PathVariable Long adresseId) {
		String statut = serviceAdresse.supprimerAdresse(adresseId);
		
		return new ResponseEntity<String>(statut, HttpStatus.OK);
	}
}
