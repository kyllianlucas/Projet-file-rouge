package com.doranco.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doranco.site.model.ArticleCommande;


@Repository
public interface ArticleCommandeRepository extends JpaRepository<ArticleCommande, Long> {

}