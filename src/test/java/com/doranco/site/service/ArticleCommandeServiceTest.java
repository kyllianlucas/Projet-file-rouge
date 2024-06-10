package com.doranco.site.service;

import com.doranco.site.model.ArticleCommande;
import com.doranco.site.repository.ArticleCommandeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ArticleCommandeServiceTest {

    @Mock
    private ArticleCommandeRepository articleCommandeRepository;

    @InjectMocks
    private ArticleCommandeService articleCommandeService;

    @Test
    public void testGetAllArticlesCommande() {
        // Création de données de test
        List<ArticleCommande> expectedArticlesCommande = new ArrayList<>();
        ArticleCommande articleCommande1 = new ArticleCommande();
        articleCommande1.setId(1L);
        articleCommande1.setQuantite(2);
        articleCommande1.setPrix(BigDecimal.valueOf(20.0));
        ArticleCommande articleCommande2 = new ArticleCommande();
        articleCommande2.setId(2L);
        articleCommande2.setQuantite(3);
        articleCommande2.setPrix(BigDecimal.valueOf(30.0));
        expectedArticlesCommande.add(articleCommande1);
        expectedArticlesCommande.add(articleCommande2);

        // Configurer le comportement du mock repository
        when(articleCommandeRepository.findAll()).thenReturn(expectedArticlesCommande);

        // Appeler la méthode du service à tester
        List<ArticleCommande> actualArticlesCommande = articleCommandeService.getAllArticlesCommande();

        // Vérifier les résultats
        assertEquals(expectedArticlesCommande.size(), actualArticlesCommande.size());
        assertEquals(expectedArticlesCommande, actualArticlesCommande);
    }

    @Test
    public void testGetArticleCommandeById() {
        // Création de données de test
        long articleCommandeId = 1L;
        ArticleCommande expectedArticleCommande = new ArticleCommande();
        expectedArticleCommande.setId(articleCommandeId);
        expectedArticleCommande.setQuantite(2);
        expectedArticleCommande.setPrix(BigDecimal.valueOf(20.0));

        // Configurer le comportement du mock repository
        when(articleCommandeRepository.findById(articleCommandeId)).thenReturn(Optional.of(expectedArticleCommande));

        // Appeler la méthode du service à tester
        Optional<ArticleCommande> actualArticleCommande = articleCommandeService.getArticleCommandeById(articleCommandeId);

        // Vérifier les résultats
        assertEquals(expectedArticleCommande, actualArticleCommande.orElse(null));
    }

    // Ajouter d'autres tests pour les autres méthodes du service...
}
