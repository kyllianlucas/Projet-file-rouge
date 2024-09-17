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
	private String image;
	private String description;
	private Integer quantite;
	private double prix;
	private double reduction;
	private double prixSpecial;
}
