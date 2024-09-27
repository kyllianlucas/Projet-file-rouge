package com.doranco.site.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleDTO {

	private Long articleId;
	private String nomProduit;
	private String description;
	private String image;
	private Integer quantite;
	private double prix;
	private double reduction;
	private double prixSpecial;
	private String marque;
}
