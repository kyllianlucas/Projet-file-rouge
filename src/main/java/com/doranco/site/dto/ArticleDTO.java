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

	private String name;
	private String description;
	private int quantite;
	private double prix;
	private String categorieName;
	private String sousCategorieName;
	private String taille;  // S, M, L, etc.
    private String genre;   // Homme, Femme, Enfant
}
