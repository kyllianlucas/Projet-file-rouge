package com.doranco.site.service;

import com.doranco.site.dto.CategorieDTO;
import com.doranco.site.dto.CategorieReponse;
import com.doranco.site.model.Categorie;


public interface CategorieService {

	CategorieDTO creerCategorie(Categorie categorie);

	CategorieReponse obtenirCategories(Integer numéroPage, Integer taillePage, String trierPar, String ordreTri);

	CategorieDTO mettreAJourCategorie(Categorie categorie, Long categorieId);

	String supprimerCategorie(Long categorieId);
}

