package com.doranco.site.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.doranco.site.dto.CommandeDTO;
import com.doranco.site.dto.CommandeReponse;

@Service
public interface CommandeService {

		
		CommandeDTO passerCommande(String emailId, Long panierId, String méthodePaiement,  String stripeToken);
		
		CommandeDTO obtenirCommande(String emailId, Long commandeId);
		
		List<CommandeDTO> obtenirCommandesParUtilisateur(String emailId);
		
		CommandeReponse obtenirToutesLesCommandes(Integer numéroPage, Integer taillePage, String trierPar, String ordreTri);
		
		CommandeDTO mettreAJourCommande(String emailId, Long commandeId, String statutCommande);
	}

