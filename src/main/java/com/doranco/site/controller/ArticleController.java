package com.doranco.site.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.doranco.site.dto.ProduitDTO;
import com.doranco.site.model.Produit;
import com.doranco.site.service.ArticleService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/api")
public class ArticleController {

	@Autowired
	private ArticleService produitService;

	@PostMapping("/admin/produit/creer")
	public ResponseEntity<ArticleDTO> ajouterProduit(@ModelAttribute ArticleRequest articleRequest ) {
		try {
		
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			Produit article = objectMapper.readValue(articleRequest.getArticleJson(), Produit.class);
	
			ArticleDTO articleEnregistre = produitService.ajouterArticle(article, articleRequest.getCategorieNom(), articleRequest.getImage());

			return new ResponseEntity<ArticleDTO>(articleEnregistre, HttpStatus.CREATED);
		}catch (Exception e) {
        // Gérer les exceptions et retourner une réponse appropriée
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/produit/all")
	public ResponseEntity<List<ArticleDTO>> obtenirTousLesArticlesSansPagination() {
	    // Récupérer tous les articles sans pagination
	    List<ArticleDTO> tousLesArticles = produitService.obtenirTousLesArticlesSansPagination();

	    // Vérifier si la liste est vide
	    if (tousLesArticles.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 si aucun article n'est trouvé
	    }
	    
	    // Retourner la liste des articles avec un code HTTP 200 (OK)
	    return new ResponseEntity<>(tousLesArticles, HttpStatus.OK);
	}

	@GetMapping("/produit/promotions")
	public ResponseEntity<List<ArticleDTO>> obtenirArticlesEnPromotion() {
	    List<ArticleDTO> articlesEnPromotion = produitService.obtenirArticlesEnPromotion();

	    if (articlesEnPromotion.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 si aucun article n'est trouvé
	    }

	    return new ResponseEntity<>(articlesEnPromotion, HttpStatus.OK); // 200 OK
	}
	
	
	@GetMapping("/public/categories/{categoryId}/produit")
	public ResponseEntity<ArticleReponse> obtenirProduitsParCategorie(@PathVariable Long categoryId,
			@RequestParam(name = "numeroPage", defaultValue = AppConfig.NUMERO_PAGE, required = false) Integer numeroPage,
			@RequestParam(name = "taillePage", defaultValue = AppConfig.TAILLE_PAGE, required = false) Integer taillePage,
			@RequestParam(name = "trierPar", defaultValue = AppConfig.TRIER_ARTICLE_PAR, required = false) String trierPar,
			@RequestParam(name = "ordreTri", defaultValue = AppConfig.ORDONNER_PAR, required = false) String ordreTri) {

		ArticleReponse reponseArticle = produitService.rechercherParCategorie(categoryId, numeroPage, taillePage, trierPar,
				ordreTri);

		return new ResponseEntity<ArticleReponse>(reponseArticle, HttpStatus.FOUND);
	}
	
	 @GetMapping("/produit/name/{productName}")
	 public ResponseEntity<Produit> getProduitByName(@PathVariable String productName) {
		 try {
		        Optional<Produit> produit = produitService.getProduitByName(productName);
		        if (produit.isPresent()) {
		            return ResponseEntity.ok(produit.get());
		        } else {
		            return ResponseEntity.notFound().build();  // Produit non trouvé
		        }
		    } catch (RuntimeException e) {
		        // Vous pouvez ici logguer l'erreur si nécessaire
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
		                             .body(null);  // Retourne une réponse 500 en cas d'erreur
		    }
	    }
	 
	@PutMapping("/admin/produit/mettreAJour")
	public ResponseEntity<ArticleDTO> mettreAJourProduit(@RequestBody ProduitDTO  produitDTO) {
		Long articleId = produitDTO.getArticleId();
	    Produit article = produitDTO.getProduit();
		ArticleDTO produitMisAJour = produitService.mettreAJourArticle(articleId, article);

		return new ResponseEntity<ArticleDTO>(produitMisAJour, HttpStatus.OK);
	}

	@PutMapping("/admin/produit/image")
	public ResponseEntity<ArticleDTO> mettreAJourImageProduit(@RequestParam ProduitDTO produitDTO) throws IOException {
		Long articleId = produitDTO.getArticleId();
		MultipartFile image = produitDTO.getImage();
		ArticleDTO articleMisAJour = produitService.mettreAJourImageArticle(articleId, image);

		return new ResponseEntity<ArticleDTO>(articleMisAJour, HttpStatus.OK);
	}

	@DeleteMapping("/admin/produit/supprimer")
	public ResponseEntity<String> supprimerProduit(@RequestBody Map<String, Long> requestBody) {
	    Long articleId = requestBody.get("articleId"); // Récupérer l'ID depuis le body
	    
	    if (articleId != null) {
	        String statut = produitService.supprimerArticle(articleId);
	        return new ResponseEntity<>(statut, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>("Article ID not provided", HttpStatus.BAD_REQUEST);
	    }
	}

}