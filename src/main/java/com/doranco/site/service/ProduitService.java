package com.doranco.site.service;

import com.doranco.site.model.Produit;
import com.doranco.site.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;

    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    public Optional<Produit> getProduitById(Long id) {
        return produitRepository.findById(id);
    }

    public Produit saveProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public void deleteProduit(Long produitId) {
        produitRepository.deleteById(produitId);
    }

    public Produit updateQuantite(Long produitId, int nouvelleQuantite) {
        Produit produit = produitRepository.findById(produitId).orElse(null);
        if (produit != null) {
            produit.setQuantiteStock(nouvelleQuantite);
            if (nouvelleQuantite == 0) {
				produit.setDisponible(false);
			}else {
				produit.setDisponible(true);
            }
            return produitRepository.save(produit);
        }
        return null;
     }   
    public Produit addProduit(Produit produit) {
        // Vérifier si le produit existe déjà
        if (produit.getId() != null && produitRepository.existsById(produit.getId())) {
            return null; // Le produit existe déjà
        }
        return produitRepository.save(produit);
    }

	public Produit findById(long id) {
	    return produitRepository.findById(id).orElse(null);
	}
}
