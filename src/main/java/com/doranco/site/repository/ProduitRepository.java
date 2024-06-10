package com.doranco.site.repository;

import java.util.List;
import java.util.Optional;

import com.doranco.site.model.Produit;

public interface ProduitRepository {

	List<Produit> findAll();

	Optional<Produit> findById(Long id);

	Produit save(Produit produit);

	void deleteById(Long produitId);

	boolean existsById(Long id);

}
