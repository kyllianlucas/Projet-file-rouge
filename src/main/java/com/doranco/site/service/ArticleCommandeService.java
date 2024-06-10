package com.doranco.site.service;

import com.doranco.site.model.ArticleCommande;
import com.doranco.site.repository.ArticleCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleCommandeService {

    @Autowired
    private ArticleCommandeRepository articleCommandeRepository;

    public List<ArticleCommande> getAllArticlesCommande() {
        return articleCommandeRepository.findAll();
    }

    public Optional<ArticleCommande> getArticleCommandeById(Long id) {
        return articleCommandeRepository.findById(id);
    }

    public ArticleCommande saveArticleCommande(ArticleCommande articleCommande) {
        return articleCommandeRepository.save(articleCommande);
    }

    public void deleteArticleCommande(Long articleCommandeId) {
        articleCommandeRepository.deleteById(articleCommandeId);
    }
}
