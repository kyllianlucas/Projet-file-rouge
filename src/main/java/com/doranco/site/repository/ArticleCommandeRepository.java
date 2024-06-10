package com.doranco.site.repository;

import java.util.List;
import java.util.Optional;

import com.doranco.site.model.ArticleCommande;

public interface ArticleCommandeRepository {

	List<ArticleCommande> findAll();

	Optional<ArticleCommande> findById(Long id);

	ArticleCommande save(ArticleCommande articleCommande);

	void deleteById(Long articleCommandeId);

}
