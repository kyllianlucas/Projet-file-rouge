package com.doranco.site.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.doranco.site.dto.ArticleDTO;
import com.doranco.site.model.Article;
import com.doranco.site.model.SousCategorie;
import com.doranco.site.repository.ArticleRepository;
import com.doranco.site.repository.CategorieRepository;

class ArticleServiceTest {

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
        Article article1 = new Article();
        Article article2 = new Article();
        when(articleRepository.findAll()).thenReturn(Arrays.asList(article1, article2));

        // Act
        List<Article> articles = articleService.getAllArticles();

        // Assert
        assertEquals(2, articles.size());
        verify(articleRepository, times(1)).findAll();
    }

    @Test
    void testGetArticlesBySousCategorie() {
        // Arrange
        Long sousCategorieId = 1L;
        Article article1 = new Article();
        Article article2 = new Article();
        when(articleRepository.findBySousCategorieId(sousCategorieId)).thenReturn(Arrays.asList(article1, article2));

        // Act
        List<Article> articles = articleService.getArticlesBySousCategorie(sousCategorieId);

        // Assert
        assertEquals(2, articles.size());
        verify(articleRepository, times(1)).findBySousCategorieId(sousCategorieId);
    }

    @Test
    void testGetArticleById_ArticleFound() {
        // Arrange
        Long articleId = 1L;
        Article article = new Article();
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(article));

        // Act
        Article foundArticle = articleService.getArticleById(articleId);

        // Assert
        assertNotNull(foundArticle);
        verify(articleRepository, times(1)).findById(articleId);
    }

    @Test
    void testGetArticleById_ArticleNotFound() {
        // Arrange
        Long articleId = 1L;
        when(articleRepository.findById(articleId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            articleService.getArticleById(articleId);
        });

        assertEquals("Article non trouvé", exception.getMessage());
        verify(articleRepository, times(1)).findById(articleId);
    }

    @Test
    void testSaveArticle() {
        // Arrange
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setName("Article Name");
        articleDTO.setDescription("Article Description");
        articleDTO.setQuantite(10);
        articleDTO.setPrix(100.0);

        Long categoryId = 1L;
        SousCategorie sousCategorie = new SousCategorie();
        when(categorieRepository.findById(categoryId)).thenReturn(Optional.of(sousCategorie));

        Article article = new Article();
        when(articleRepository.save(any(Article.class))).thenReturn(article);

        // Act
        Article savedArticle = articleService.saveArticle(articleDTO, categoryId);

        // Assert
        assertNotNull(savedArticle);
        verify(categorieRepository, times(1)).findById(categoryId);
        verify(articleRepository, times(1)).save(any(Article.class));
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
    void testUpdateArticle() {
        // Arrange
        Long articleId = 1L;
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setName("Updated Name");
        articleDTO.setDescription("Updated Description");
        articleDTO.setQuantite(20);
        articleDTO.setPrix(200.0);

        Article existingArticle = new Article();
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(existingArticle));

        Long categoryId = 1L;
        SousCategorie sousCategorie = new SousCategorie();
        when(categorieRepository.findById(categoryId)).thenReturn(Optional.of(sousCategorie));

        when(articleRepository.save(existingArticle)).thenReturn(existingArticle);

        // Act
        Article updatedArticle = articleService.updateArticle(articleId, articleDTO, categoryId);

        // Assert
        assertNotNull(updatedArticle);
        assertEquals("Updated Name", updatedArticle.getName());
        assertEquals("Updated Description", updatedArticle.getDescription());
        assertEquals(20, updatedArticle.getQuantityInStock());
        assertEquals(200.0, updatedArticle.getPrix());

        verify(articleRepository, times(1)).findById(articleId);
        verify(categorieRepository, times(1)).findById(categoryId);
        verify(articleRepository, times(1)).save(existingArticle);
    }
}
