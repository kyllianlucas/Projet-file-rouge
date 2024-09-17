package com.doranco.site.service;

import com.doranco.site.dto.CategorieDTO;
import com.doranco.site.dto.CategorieReponse;
import com.doranco.site.model.Categorie;


public interface CategorieService {

	CategorieDTO créerCategorie(Categorie categorie);

	CategorieReponse obtenirCategories(Integer numéroPage, Integer taillePage, String trierPar, String ordreTri);

	CategorieDTO mettreÀJourCategorie(Categorie categorie, Long categorieId);

	String supprimerCategorie(Long categorieId);
}

