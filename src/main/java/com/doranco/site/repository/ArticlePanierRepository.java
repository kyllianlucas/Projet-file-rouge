package com.doranco.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.doranco.site.model.Article;
import com.doranco.site.model.ArticlePanier;

@Repository
public interface ArticlePanierRepository extends JpaRepository<ArticlePanier, Long> {
	
	@Query("SELECT ap.produit FROM ArticlePanier ap WHERE ap.produit.id = ?1")
	Article findProductById(Long produitId);
	
	@Query("SELECT ap FROM ArticlePanier ap WHERE ap.panier.id = ?1 AND ap.produit.id = ?2")
	ArticlePanier findCartItemByProductIdAndCartId(Long panierId, Long produitId);
	
	@Modifying
    @Query("DELETE FROM ArticlePanier ap WHERE ap.panier.id = ?1 AND ap.produit.id = ?2")
    void deleteCartItemByProductIdAndCartId(Long produitId, Long panierId);
}