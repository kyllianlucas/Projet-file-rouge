package com.doranco.site.service;

import java.util.List;
import com.doranco.site.dto.PanierDTO;

public interface PanierService {
	
	PanierDTO ajouterArticleAuPanier(Long panierId, Long produitId, Integer quantité);
	
	List<PanierDTO> obtenirTousLesPaniers();
	
	PanierDTO obtenirPanier(String emailId, Long panierId);
	
	PanierDTO mettreAJourQuantitéArticleDansPanier(Long panierId, Long produitId, Integer quantité);
	
	void mettreAJourArticleDansPaniers(Long panierId, Long produitId);
	
	String supprimerArticleDuPanier(Long panierId, Long produitId);
	
}