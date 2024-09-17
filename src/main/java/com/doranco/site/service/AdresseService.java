package com.doranco.site.service;

import java.util.List;

import com.doranco.site.dto.AdresseDTO;
import com.doranco.site.model.Adresse;

public interface AdresseService {
    
    AdresseDTO créerAdresse(AdresseDTO adresseDTO);
    
    List<AdresseDTO> obtenirAdresses();
    
    AdresseDTO obtenirAdresse(Long adresseId);
    
    AdresseDTO mettreÀJourAdresse(Long adresseId, Adresse adresse);
    
    String supprimerAdresse(Long adresseId);
}