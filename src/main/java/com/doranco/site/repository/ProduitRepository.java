package com.doranco.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doranco.site.model.Produit;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long>{

	

}
