package com.doranco.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategorieDTO {

	private Long categorieId;
	private String nomCategorie;
//	private List<ArticleDTO> articles = new ArrayList<>();
}
