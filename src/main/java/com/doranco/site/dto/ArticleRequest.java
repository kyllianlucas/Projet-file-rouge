package com.doranco.site.dto;

import com.doranco.site.model.Produit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequest {

	Produit article;
	String CategorieNom;
}
