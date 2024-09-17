package com.doranco.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticlePanierDTO {
	
	private Long articlePanierId;
	private PanierDTO panier;
	private ArticleDTO produit;
	private Integer quantite;
	private double reduction;
	private double prixProduit;
}
