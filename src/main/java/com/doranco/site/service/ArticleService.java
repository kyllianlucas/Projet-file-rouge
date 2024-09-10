package com.doranco.site.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.doranco.site.dto.ArticleDTO;
import com.doranco.site.model.Article;
import com.doranco.site.model.Categorie;
import com.doranco.site.model.SousCategorie;
import com.doranco.site.repository.ArticleRepository;
import com.doranco.site.repository.CategorieRepository;
import com.doranco.site.repository.SousCategorieRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
@Transactional
public class ArticleService {

	@Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private SousCategorieRepository sousCategorieRepository;

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

    public Article saveArticle(ArticleDTO articleDTO) {
        // Crée un nouvel article à partir du DTO
        Article article = new Article();
        article.setName(articleDTO.getName());
        article.setDescription(articleDTO.getDescription());
        article.setQuantite(articleDTO.getQuantite());
        article.setPrix(articleDTO.getPrix());

        // Recherche la catégorie par son nom (si présente)
        if (articleDTO.getCategorieName() != null) {
            Categorie category = categorieRepository.findByName(articleDTO.getCategorieName())
                    .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

            // Associe la catégorie à l'article
            article.setCategorie(category);

            // Si la catégorie est "Tee-shirt", on recherche la sous-catégorie
            if ("Tee-shirt".equalsIgnoreCase(articleDTO.getCategorieName()) && articleDTO.getSousCategorieName() != null) {
                // Recherche la sous-catégorie par son nom et catégorie
                SousCategorie sousCategory = sousCategorieRepository.findByNameAndCategorie(
                        articleDTO.getSousCategorieName(), category)
                        .orElseThrow(() -> new RuntimeException("Sous-catégorie non trouvée"));

                // On ajoute les attributs supplémentaires à la sous-catégorie (taille et genre)
                sousCategory.setTaille(articleDTO.getTaille());
                sousCategory.setGenre(articleDTO.getGenre());

                // Associe la sous-catégorie à l'article
                article.setSousCategorie(sousCategory);
            }
        }

        // Sauvegarde l'article dans le repository
        return articleRepository.save(article);
    }

    public Article updateArticle(Long id, ArticleDTO articleDTO, Long categoryId, MultipartFile imageFile) {
        Article existingArticle = articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article non trouvé"));

        existingArticle.setName(articleDTO.getName());
        existingArticle.setDescription(articleDTO.getDescription());
        existingArticle.setPrix(articleDTO.getPrix());
        existingArticle.setQuantite(articleDTO.getQuantite());

        if (categoryId != null) {
            Categorie categorie = categorieRepository.findById(categoryId)
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
