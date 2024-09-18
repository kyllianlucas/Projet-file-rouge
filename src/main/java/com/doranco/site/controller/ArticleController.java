package com.doranco.site.controller;

import java.io.IOException;

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
import org.springframework.web.multipart.MultipartFile;

import com.doranco.site.config.AppConfig;
import com.doranco.site.dto.ArticleDTO;
import com.doranco.site.dto.ArticleReponse;
import com.doranco.site.dto.ArticleRequest;
import com.doranco.site.model.Produit;
import com.doranco.site.service.ArticleService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ArticleController {

	@Autowired
	private ArticleService produitService;

	@PostMapping("/admin/produit/creer")
	public ResponseEntity<ArticleDTO> ajouterProduit(@Valid @RequestBody ArticleRequest articleRequest) {

		ArticleDTO articleEnregistre = produitService.ajouterArticle(articleRequest.getCategorieNom(), articleRequest.getArticle());

		return new ResponseEntity<ArticleDTO>(articleEnregistre, HttpStatus.CREATED);
	}

	@GetMapping("/public/produits")
	public ResponseEntity<ArticleReponse> obtenirTousLesProduits(
			@RequestParam(name = "numeroPage", defaultValue = AppConfig.NUMERO_PAGE, required = false) Integer numeroPage,
			@RequestParam(name = "taillePage", defaultValue = AppConfig.TAILLE_PAGE, required = false) Integer taillePage,
			@RequestParam(name = "trierPar", defaultValue = AppConfig.TRIER_ARTICLE_PAR, required = false) String trierPar,
			@RequestParam(name = "ordreTri", defaultValue = AppConfig.ORDONNER_PAR, required = false) String ordreTri) {

		ArticleReponse reponseArticle = produitService.obtenirTousLesArticles(numeroPage, taillePage, trierPar, ordreTri);

		return new ResponseEntity<ArticleReponse>(reponseArticle, HttpStatus.FOUND);
	}

	@GetMapping("/public/categories/{categoryId}/produits")
	public ResponseEntity<ArticleReponse> obtenirProduitsParCategorie(@PathVariable Long categoryId,
			@RequestParam(name = "numeroPage", defaultValue = AppConfig.NUMERO_PAGE, required = false) Integer numeroPage,
			@RequestParam(name = "taillePage", defaultValue = AppConfig.TAILLE_PAGE, required = false) Integer taillePage,
			@RequestParam(name = "trierPar", defaultValue = AppConfig.TRIER_ARTICLE_PAR, required = false) String trierPar,
			@RequestParam(name = "ordreTri", defaultValue = AppConfig.ORDONNER_PAR, required = false) String ordreTri) {

		ArticleReponse reponseArticle = produitService.rechercherParCategorie(categoryId, numeroPage, taillePage, trierPar,
				ordreTri);

		return new ResponseEntity<ArticleReponse>(reponseArticle, HttpStatus.FOUND);
	}

	@GetMapping("/public/produits/motcle/{motcle}")
	public ResponseEntity<ArticleReponse> obtenirProduitsParMotCle(@PathVariable String motcle,
			@RequestParam(name = "numeroPage", defaultValue = AppConfig.NUMERO_PAGE, required = false) Integer numeroPage,
			@RequestParam(name = "taillePage", defaultValue = AppConfig.TAILLE_PAGE, required = false) Integer taillePage,
			@RequestParam(name = "trierPar", defaultValue = AppConfig.TRIER_ARTICLE_PAR, required = false) String trierPar,
			@RequestParam(name = "ordreTri", defaultValue = AppConfig.ORDONNER_PAR, required = false) String ordreTri) {

		ArticleReponse reponseArticle = produitService.rechercherArticleParMotCle(motcle, numeroPage, taillePage, trierPar,
				ordreTri);

		return new ResponseEntity<ArticleReponse>(reponseArticle, HttpStatus.FOUND);
	}

	@PutMapping("/admin/produits/mettreAJour/{productId}")
	public ResponseEntity<ArticleDTO> mettreAJourProduit(@RequestBody Produit article,
			@PathVariable Long articleId) {
		ArticleDTO produitMisAJour = produitService.mettreAJourArticle(articleId, article);

		return new ResponseEntity<ArticleDTO>(produitMisAJour, HttpStatus.OK);
	}

	@PutMapping("/admin/produits/{productId}/image")
	public ResponseEntity<ArticleDTO> mettreAJourImageProduit(@PathVariable Long articleId, @RequestParam("image") MultipartFile image) throws IOException {
		ArticleDTO articleMisAJour = produitService.mettreAJourImageArticle(articleId, image);

		return new ResponseEntity<ArticleDTO>(articleMisAJour, HttpStatus.OK);
	}

	@DeleteMapping("/admin/produits/supprimer/{productId}")
	public ResponseEntity<String> supprimerProduitParCategorie(@PathVariable Long articleId) {
		String statut = produitService.supprimerArticle(articleId);

		return new ResponseEntity<String>(statut, HttpStatus.OK);
	}

}