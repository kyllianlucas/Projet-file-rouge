package com.doranco.site.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.doranco.site.model.Article;
import com.doranco.site.service.ArticleService;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@AllArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/bySousCategorie/{sousCategorieId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<Article> getArticlesBySousCategorie(@PathVariable Long sousCategorieId) {
        return articleService.getArticlesBySousCategorie(sousCategorieId);
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id) {
        Article article = articleService.getArticleById(id);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/creerArticle")
    public ResponseEntity<Article> createArticle(@RequestParam String name, @RequestParam String description,
                                                 @RequestParam int quantite, @RequestParam double prix,
                                                 @RequestParam Long categoryId) {
        Article newArticle = articleService.saveArticle(name, description, quantite, prix, categoryId);
        return new ResponseEntity<>(newArticle, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/majArticle")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestParam String name,
                                                 @RequestParam String description, @RequestParam int quantite,
                                                 @RequestParam double prix, @RequestParam(required = false) Long categoryId) {
        Article updatedArticle = articleService.updateArticle(id, name, description, quantite, prix, categoryId);
        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/supprimerArticle")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
