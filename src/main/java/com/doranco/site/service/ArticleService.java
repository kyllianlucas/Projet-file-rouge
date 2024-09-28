package com.doranco.site.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.web.multipart.MultipartFile;

import com.doranco.site.dto.ArticleDTO;
import com.doranco.site.dto.ArticleReponse;
import com.doranco.site.model.Produit;


public interface ArticleService {

		ArticleDTO ajouterArticle(Produit article, String categorieNom, MultipartFile image) throws IOException;

		ArticleReponse rechercherParCategorie(Long catégorieId, Integer numéroPage, Integer taillePage, String trierPar,
				String ordreTri);

		ArticleDTO mettreAJourArticle(Long articleId, Produit article);

		ArticleDTO mettreAJourImageArticle(Long articleId, MultipartFile image) throws IOException;

		ArticleReponse rechercherArticleParMotCle(String motClé, Integer numéroPage, Integer taillePage, String trierPar,
				String ordreTri);

		String supprimerArticle(Long articleId);

		List<ArticleDTO> obtenirTousLesArticlesSansPagination();

		List<ArticleDTO> obtenirArticlesEnPromotion();

		Optional<Produit> getProduitByName(String productName);

}
