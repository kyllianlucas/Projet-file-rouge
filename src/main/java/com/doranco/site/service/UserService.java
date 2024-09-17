package com.doranco.site.service;

import com.doranco.site.dto.UtilisateurDTO;
import com.doranco.site.dto.UtilisateurReponse;

public interface UserService {

    UtilisateurDTO enregistrerUtilisateur(UtilisateurDTO utilisateurDTO);

    UtilisateurReponse obtenirTousLesUtilisateurs(Integer numeroPage, Integer taillePage, String trierPar, String ordreTrier);

    UtilisateurDTO obtenirUtilisateurParId(Long utilisateurId);

    UtilisateurDTO mettreAJourUtilisateur(Long utilisateurId, UtilisateurDTO utilisateurDTO);

    String supprimerUtilisateur(Long utilisateurId);
}