package com.doranco.site.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doranco.site.model.Panier;
import com.doranco.site.model.Utilisateur;

public interface PanierRepository extends JpaRepository<Panier,Long>{

	Optional<Panier> findByUser(Utilisateur user);

}
