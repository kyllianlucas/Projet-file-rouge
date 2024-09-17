package com.doranco.site.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.doranco.site.dto.ArticleDTO;
import com.doranco.site.dto.ArticleReponse;
import com.doranco.site.model.Article;


public interface ArticleService {

		ArticleDTO ajouterArticle(String categorieNom, Article article);

		ArticleReponse obtenirTousLesArticles(Integer numéroPage, Integer taillePage, String trierPar, String ordreTri);

		ArticleReponse rechercherParCatégorie(Long catégorieId, Integer numéroPage, Integer taillePage, String trierPar,
				String ordreTri);

		ArticleDTO mettreÀJourArticle(Long articleId, Article article);

		ArticleDTO mettreÀJourImageArticle(Long articleId, MultipartFile image) throws IOException;

		ArticleReponse rechercherArticleParMotClé(String motClé, Integer numéroPage, Integer taillePage, String trierPar,
				String ordreTri);

		String supprimerArticle(Long articleId);

}
