package com.doranco.site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doranco.site.model.Commande;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
	
	@Query("SELECT c FROM Commande c WHERE c.email = ?1 AND c.id = ?2")
	Commande findCommandeByEmailAndCommandeId(String email, Long commandeId);

	List<Commande> findAllByEmail(String email);
	
}
