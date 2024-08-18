package com.doranco.site.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.doranco.site.dto.ArticleDTO;
import com.doranco.site.model.Article;
import com.doranco.site.model.SousCategorie;
import com.doranco.site.repository.ArticleRepository;
import com.doranco.site.repository.CategorieRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class ArticleService {

	private final ArticleRepository articleRepository;
	private final CategorieRepository categorieRepository;
	
	public List<Article> getAllArticles() {
	     return articleRepository.findAll();
	}
	
	 public List<Article> getArticlesBySousCategorie(Long sousCategorieId) {
	        return articleRepository.findBySousCategorieId(sousCategorieId);
	    }
	
    public Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé"));
    }

    public Article saveArticle(ArticleDTO articleDTO, Long categoryId) {
        Article article =  new Article();
        article.setName(articleDTO.getName());
        article.setDescription(articleDTO.getDescription());
    	article.setQuantityInStock(articleDTO.getQuantite());
    	article.setPrix(articleDTO.getPrix());
    	
    	SousCategorie sousCategory = categorieRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));;
    	article.setSousCategorie(sousCategory);
    	return articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    public Article updateArticle(Long id, ArticleDTO articleDTO, Long categoryId) {
        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé"));
        
        existingArticle.setName(articleDTO.getName());
        existingArticle.setDescription(articleDTO.getDescription());
        existingArticle.setPrix(articleDTO.getPrix());
        existingArticle.setQuantityInStock(articleDTO.getQuantite());
        
        if (categoryId != null) {
			SousCategorie categorie = categorieRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Categories non trouvée"));
			existingArticle.setSousCategorie(categorie);
		}
        return articleRepository.save(existingArticle);
    }
}
