package com.doranco.site.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.doranco.site.dto.ArticleDTO;
import com.doranco.site.model.Article;
import com.doranco.site.service.ArticleService;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/users/articles")
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
    @PostMapping("admin/creerArticle")
    public ResponseEntity<Article> createArticle(
            @RequestPart("article") ArticleDTO articleDTO
    ) {
        Article newArticle = articleService.saveArticle(articleDTO);
        return new ResponseEntity<>(newArticle, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/majArticle/{id}")
    public ResponseEntity<Article> updateArticle(
            @PathVariable Long id,
            @RequestPart("article") ArticleDTO articleDTO,
            @RequestParam(required = false) Long categoryId
    ) {
        Article updatedArticle = articleService.updateArticle(id, articleDTO);
        return new ResponseEntity<>(updatedArticle, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/supprimerArticle/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
