package com.doranco.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleCommandeDTO {

	private Long articleCommandeId;
	private ArticleDTO article;
	private Integer quantite;
	private double remise;
	private double prixProduitCommande;
}
