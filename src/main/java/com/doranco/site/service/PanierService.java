package com.doranco.site.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.doranco.site.model.Article;
import com.doranco.site.model.Item;
import com.doranco.site.model.Panier;
import com.doranco.site.model.Utilisateur;
import com.doranco.site.repository.ArticleRepository;
import com.doranco.site.repository.PanierItemRepository;
import com.doranco.site.repository.PanierRepository;
import com.doranco.site.repository.UtilisateurRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class PanierService {

	private final PanierRepository panierRepository;
	private final PanierItemRepository panierItemRepository;
	private final ArticleRepository articleRepository;
	private final UtilisateurRepository userRepository;
	
	public Panier getPanier(Utilisateur user) {
		return panierRepository.findByUser(user).orElseGet(() -> createPanier(user));
	}
	
	public Panier addToPanier(Utilisateur user, Long articleId, int quantity) {
		Panier panier = getPanier(user);
		Article article = articleRepository.findById(articleId).orElseThrow(() -> new RuntimeException("Article non trouvé"));

		Optional<Item> existingItem = panier.getItems().stream()
                .filter(item -> item.getArticle().getId().equals(articleId))
                .findFirst();

        if (existingItem.isPresent()) {
            Item panierItem = existingItem.get();
            panierItem.setQuantity(panierItem.getQuantity() + quantity);
        } else {
        	Item partItem = new Item();
        	partItem.setPanier(panier);
        	partItem.setArticle(article);
        	partItem.setQuantity(quantity);
        	panier.getItems().add(partItem);
        }

        return panierRepository.save(panier);
    }

    public Panier removeFromPanier(Utilisateur user, Long panierItemId) {
        Panier panier = getPanier(user);
        panier.getItems().removeIf(item -> item.getId().equals(panierItemId));
        panierItemRepository.deleteById(panierItemId);
        return panierRepository.save(panier);
    }

    public void clearPanier(Utilisateur user) {
        Panier panier = getPanier(user);
        panier.getItems().clear();
        panierRepository.save(panier);
    }

    private Panier createPanier(Utilisateur user) {
        Panier panier = new Panier();
        panier.setUser(user);
        return panierRepository.save(panier);
    }
}
