package com.doranco.site.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doranco.site.config.AppConfig;
import com.doranco.site.dto.CategorieDTO;
import com.doranco.site.dto.CategorieReponse;
import com.doranco.site.model.Categorie;
import com.doranco.site.service.CategorieService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CategorieController {

	@Autowired
	private CategorieService serviceCategorie;

	@PostMapping("/admin/categorie/creer")
	public ResponseEntity<CategorieDTO> creerCategorie(@Valid @RequestBody Categorie categorie) {
		CategorieDTO categorieEnregistree = serviceCategorie.creerCategorie(categorie);

		return new ResponseEntity<CategorieDTO>(categorieEnregistree, HttpStatus.CREATED);
	}

	@GetMapping("/public/categories")
	public ResponseEntity<CategorieReponse> obtenirCategories(
			@RequestParam(name = "numeroPage", defaultValue = AppConfig.NUMERO_PAGE, required = false) Integer numeroPage,
			@RequestParam(name = "taillePage", defaultValue = AppConfig.TAILLE_PAGE, required = false) Integer taillePage,
			@RequestParam(name = "trierPar", defaultValue = AppConfig.TRIER_CATEGORIES_PAR, required = false) String trierPar,
			@RequestParam(name = "ordreTri", defaultValue = AppConfig.ORDONNER_PAR, required = false) String ordreTri) {
		
		CategorieReponse reponseCategorie = serviceCategorie.obtenirCategories(numeroPage, taillePage, trierPar, ordreTri);

		return new ResponseEntity<CategorieReponse>(reponseCategorie, HttpStatus.FOUND);
	}

	@PutMapping("/admin/categories/{categoryId}")
	public ResponseEntity<CategorieDTO> mettreAJourCategorie(@RequestBody Categorie categorie,
			@PathVariable Long categoryId) {
		CategorieDTO categorieDTO = serviceCategorie.mettreAJourCategorie(categorie, categoryId);

		return new ResponseEntity<CategorieDTO>(categorieDTO, HttpStatus.OK);
	}

	@DeleteMapping("/admin/categories/{categoryId}")
	public ResponseEntity<String> supprimerCategorie(@PathVariable Long categoryId) {
		String status = serviceCategorie.supprimerCategorie(categoryId);

		return new ResponseEntity<String>(status, HttpStatus.OK);
	}

}
