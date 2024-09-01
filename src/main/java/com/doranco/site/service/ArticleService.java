package com.doranco.site.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

    public Article saveArticle(ArticleDTO articleDTO, Long categoryId, MultipartFile imageFile) {
        Article article = new Article();
        article.setName(articleDTO.getName());
        article.setDescription(articleDTO.getDescription());
        article.setQuantityInStock(articleDTO.getQuantite());
        article.setPrix(articleDTO.getPrix());

        if (categoryId != null) {
            SousCategorie sousCategory = categorieRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
            article.setSousCategorie(sousCategory);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            article.setImage(imagePath);
        }

        return articleRepository.save(article);
    }

    public Article updateArticle(Long id, ArticleDTO articleDTO, Long categoryId, MultipartFile imageFile) {
        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé"));

        existingArticle.setName(articleDTO.getName());
        existingArticle.setDescription(articleDTO.getDescription());
        existingArticle.setPrix(articleDTO.getPrix());
        existingArticle.setQuantityInStock(articleDTO.getQuantite());

        if (categoryId != null) {
            SousCategorie categorie = categorieRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));
            existingArticle.setSousCategorie(categorie);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage(imageFile);
            existingArticle.setImage(imagePath);
        }

        return articleRepository.save(existingArticle);
    }

    public void deleteArticle(Long id) {
        articleRepository.deleteById(id);
    }

    private String saveImage(MultipartFile imageFile) {
        try {
            String imageDir = "uploads/";
            String imageName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            Path imagePath = Paths.get(imageDir, imageName);

            Files.createDirectories(imagePath.getParent());
            Files.write(imagePath, imageFile.getBytes());

            return imagePath.toString();
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement de l'image", e);
        }
    }
}
