package com.doranco.site.service;

import java.util.List;

import org.springframework.stereotype.Service;

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

    public Article saveArticle(String name, String description, int quantite, double prix, Long categoryId) {
        Article article =  new Article();
        article.setName(name);
        article.setDescription(description);
    	article.setQuantityInStock(quantite);
    	article.setPrix(prix);
    	
    	SousCategorie sousCategory = categorieRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));;
    	article.setSousCategorie(sousCategory);
    	return articleRepository.save(article);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    public Article updateArticle(Long id, String name, String description, int quantite, double prix, Long categoryId) {
        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé"));
        
        existingArticle.setName(name);
        existingArticle.setDescription(description);
        existingArticle.setPrix(prix);
        existingArticle.setQuantityInStock(quantite);
        
        if (categoryId != null) {
			SousCategorie categorie = categorieRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("Categories non trouvée"));
			existingArticle.setSousCategorie(categorie);
		}
        return articleRepository.save(existingArticle);
    }
}
