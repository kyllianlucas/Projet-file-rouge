package com.doranco.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doranco.site.model.Categorie;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {

	Categorie findByCategoryName(String categoryName);

}