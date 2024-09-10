package com.doranco.site.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doranco.site.model.Categorie;

public interface CategorieRepository extends JpaRepository<Categorie, Long>{

	Optional<Categorie> findByName(String categorieName);

}
