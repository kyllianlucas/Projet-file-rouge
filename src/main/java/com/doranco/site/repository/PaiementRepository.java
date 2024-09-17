package com.doranco.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.doranco.site.model.Paiement;


@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {

}