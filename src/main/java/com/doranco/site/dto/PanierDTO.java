package com.doranco.site.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PanierDTO {
	
	private Long panierId;
	private Double prixTotal = 0.0;
	private List<ArticleDTO> articles = new ArrayList<>();
}
