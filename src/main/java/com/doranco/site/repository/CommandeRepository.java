package com.doranco.site.repository;

import java.util.List;
import java.util.Optional;

import com.doranco.site.model.Commande;

public interface CommandeRepository {

	List<Commande> findAll();

	Optional<Commande> findById(Long id);

	Commande save(Commande commande);

	void deleteById(Long commandeId);

}
