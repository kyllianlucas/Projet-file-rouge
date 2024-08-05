package com.doranco.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doranco.site.model.SousCategorie;

public interface CategorieRepository extends JpaRepository<SousCategorie, Long>{

}
