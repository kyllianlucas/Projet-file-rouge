package com.doranco.site.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.doranco.site.model.Utilisateur;


@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

    @Query("SELECT u FROM Utilisateur u JOIN FETCH u.adresses a WHERE a.idAdresse = ?1")
    List<Utilisateur> findByAdresses(Long idAdresse);

    Optional<Utilisateur>findByEmail(String email);
}
