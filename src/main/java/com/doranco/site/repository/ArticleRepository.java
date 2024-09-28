package com.doranco.site.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.doranco.site.model.Produit;

public interface ArticleRepository extends JpaRepository<Produit, Long> {

	Page<Produit> findByProductNameLike(String keyword, Pageable pageDetails);
	
	@Query("SELECT p FROM Produit p WHERE LOWER(p.productName) = LOWER(:productName)")
	Optional<Produit> findByProductNameIgnoreCase(@Param("productName") String productName);
}
