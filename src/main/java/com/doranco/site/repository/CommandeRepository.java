package com.doranco.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.doranco.site.model.Commande;

public interface CommandeRepository extends JpaRepository<Commande, Long>{

}
