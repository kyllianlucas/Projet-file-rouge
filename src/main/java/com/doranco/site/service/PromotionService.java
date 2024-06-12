package com.doranco.site.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.doranco.site.model.Promotion;
import com.doranco.site.model.Produit;
import com.doranco.site.repository.PromotionRepository;
import com.doranco.site.repository.ProduitRepository;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ProduitRepository produitRepository;

    public Promotion savePromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    public void deletePromotion(Long id) {
        promotionRepository.deleteById(id);
    }

    public Promotion getPromotionById(Long id) {
        return promotionRepository.findById(id).orElse(null);
    }

    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }

    public Promotion updatePromotion(Long id, Promotion promotionDetails) {
        Promotion promotion = promotionRepository.findById(id).orElse(null);
        if (promotion != null) {
            promotion.setCode(promotionDetails.getCode());
            promotion.setDescription(promotionDetails.getDescription());
            promotion.setPourcentageReduction(promotionDetails.getPourcentageReduction());
            promotion.setDateDebut(promotionDetails.getDateDebut());
            promotion.setDateFin(promotionDetails.getDateFin());
            
            BigDecimal prixTotal = BigDecimal.ZERO;
            if (promotion.getProduits() != null) {
            	for (Produit produit : promotion.getProduits()) {
					BigDecimal prix = produit.getPrix();
					BigDecimal pourcentageReduction = new BigDecimal(promotion.getPourcentageReduction());
					BigDecimal reduction = prix.multiply(pourcentageReduction.divide(new BigDecimal(100)));
					BigDecimal prixFinal = prix.subtract(reduction);
					prixTotal = prixTotal.add(prixFinal);
            	}
			}
            promotion.setPrix(prixTotal);         
            return promotionRepository.save(promotion);
        } else {
            return null;
        }
    }

}
