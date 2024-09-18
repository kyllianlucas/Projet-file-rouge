package com.doranco.site.service;

import java.util.List;

import com.doranco.site.dto.AdresseDTO;
import com.doranco.site.model.Adresse;

public interface AdresseService {
    
    AdresseDTO creerAdresse(AdresseDTO adresseDTO);
    
    List<AdresseDTO> obtenirAdresses();
    
    AdresseDTO obtenirAdresse(Long adresseId);
    
    AdresseDTO mettreAJourAdresse(Long adresseId, Adresse adresse);
    
    String supprimerAdresse(Long adresseId);
}