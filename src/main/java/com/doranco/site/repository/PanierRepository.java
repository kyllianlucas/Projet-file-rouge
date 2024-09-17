package com.doranco.site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.doranco.site.model.Panier;

public interface PanierRepository extends JpaRepository<Panier,Long>{

	@Query("SELECT p FROM Panier p WHERE p.utilisateur.email = ?1 AND p.id = ?2")
	Panier findCartByEmailAndCartId(String email, Long panierId);

	@Query("SELECT p FROM Panier p JOIN FETCH p.itemsPanier i JOIN FETCH i.produit pr WHERE pr.id = ?1")
	List<Panier> findCartsByProductId(Long produitId);

}
