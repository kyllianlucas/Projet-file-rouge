package com.doranco.site.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doranco.site.model.Categorie;
import com.doranco.site.model.SousCategorie;

@Repository
public interface SousCategorieRepository extends JpaRepository<SousCategorie, Long>{

	Optional<SousCategorie> findByNameAndCategorie(String sousCategorieName, Categorie category);

}
