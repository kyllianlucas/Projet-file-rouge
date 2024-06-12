package com.doranco.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doranco.site.model.Commande;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long>{

	

}
