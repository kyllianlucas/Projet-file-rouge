package com.doranco.site.dto;

import com.doranco.site.model.Article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRequest {

	Article article;
	String CategorieNom;
}
