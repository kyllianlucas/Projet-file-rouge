package com.doranco.site.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.doranco.site.model.Article;
import com.doranco.site.model.SousCategorie;
import com.doranco.site.repository.ArticleRepository;
import com.doranco.site.repository.CategorieRepository;

public class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private CategorieRepository categorieRepository;

    @InjectMocks
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllArticles() {
        // Arrange
        List<Article> articles = new ArrayList<>();
        articles.add(new Article());
        when(articleRepository.findAll()).thenReturn(articles);

        // Act
        List<Article> result = articleService.getAllArticles();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetArticlesBySousCategorie() {
        // Arrange
        Long sousCategorieId = 1L;
        List<Article> articles = new ArrayList<>();
        articles.add(new Article());
        when(articleRepository.findBySousCategorieId(sousCategorieId)).thenReturn(articles);

        // Act
        List<Article> result = articleService.getArticlesBySousCategorie(sousCategorieId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetArticleById_Success() {
        // Arrange
        Long articleId = 1L;
        Article article = new Article();
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        // Act
        Article result = articleService.getArticleById(articleId);

        // Assert
        assertNotNull(result);
        assertEquals(article, result);
    }

    @Test
    void testGetArticleById_NotFound() {
        // Arrange
        Long articleId = 1L;
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            articleService.getArticleById(articleId);
        });
        assertEquals("Article non trouvé", exception.getMessage());
    }

    @Test
    void testSaveArticle_Success() {
        // Arrange
        String name = "Article";
        String description = "Description";
        int quantite = 10;
        double prix = 100.0;
        Long categoryId = 1L;
        SousCategorie sousCategorie = new SousCategorie();
        when(categorieRepository.findById(categoryId)).thenReturn(Optional.of(sousCategorie));

        Article article = new Article();
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        // Act
        Article result = articleService.saveArticle(name, description, quantite, prix, categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(article, result);
    }

    @Test
    void testSaveArticle_CategorieNotFound() {
        // Arrange
        String name = "Article";
        String description = "Description";
        int quantite = 10;
        double prix = 100.0;
        Long categoryId = 1L;
        when(categorieRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            articleService.saveArticle(name, description, quantite, prix, categoryId);
        });
        assertEquals("Catégorie non trouvée", exception.getMessage());
    }

    @Test
    void testDeleteArticle() {
        // Arrange
        Long articleId = 1L;

        // Act
        articleService.deleteArticle(articleId);

        // Assert
        verify(articleRepository, times(1)).deleteById(articleId);
    }

    @Test
    void testUpdateArticle_Success() {
        // Arrange
        Long articleId = 1L;
        String name = "Updated Article";
        String description = "Updated Description";
        int quantite = 5;
        double prix = 50.0;
        Long categoryId = 1L;

        Article existingArticle = new Article();
        existingArticle.setId(articleId);
        existingArticle.setSousCategorie(new SousCategorie());
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));

        SousCategorie sousCategorie = new SousCategorie();
        when(categorieRepository.findById(categoryId)).thenReturn(Optional.of(sousCategorie));

        when(articleRepository.save(any(Article.class))).thenReturn(existingArticle);

        // Act
        Article result = articleService.updateArticle(articleId, name, description, quantite, prix, categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals(description, result.getDescription());
        assertEquals(quantite, result.getQuantityInStock());
        assertEquals(prix, result.getPrix());
        assertEquals(sousCategorie, result.getSousCategorie());
    }

    @Test
    void testUpdateArticle_ArticleNotFound() {
        // Arrange
        Long articleId = 1L;
        String name = "Updated Article";
        String description = "Updated Description";
        int quantite = 5;
        double prix = 50.0;
        Long categoryId = 1L;

        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            articleService.updateArticle(articleId, name, description, quantite, prix, categoryId);
        });
        assertEquals("Article non trouvé", exception.getMessage());
    }

    @Test
    void testUpdateArticle_CategorieNotFound() {
        // Arrange
        Long articleId = 1L;
        String name = "Updated Article";
        String description = "Updated Description";
        int quantite = 5;
        double prix = 50.0;
        Long categoryId = 1L;

        Article existingArticle = new Article();
        existingArticle.setId(articleId);
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));

        when(categorieRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            articleService.updateArticle(articleId, name, description, quantite, prix, categoryId);
        });
        assertEquals("Categories non trouvée", exception.getMessage());
    }
}
