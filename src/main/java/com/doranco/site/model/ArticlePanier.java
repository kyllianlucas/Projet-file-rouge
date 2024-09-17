package com.doranco.site.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "articles_du_panier")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePanier {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idArticleDuPanier;
	
	@ManyToOne
	@JoinColumn(name = "id_panier")
	private Panier panier;
	
	@ManyToOne
	@JoinColumn(name = "id_produit")
	private Article produit;
	
	private Integer quantite;
	private double remise;
	private double prixProduit;
	
}
