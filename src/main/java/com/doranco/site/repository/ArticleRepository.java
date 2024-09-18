package com.doranco.site.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.doranco.site.model.Produit;

public interface ArticleRepository extends JpaRepository<Produit, Long> {

	Page<Produit> findByProductNameLike(String keyword, Pageable pageDetails);

}
