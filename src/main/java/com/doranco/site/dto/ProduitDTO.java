package com.doranco.site.dto;

import org.springframework.web.multipart.MultipartFile;

import com.doranco.site.model.Produit;

import lombok.Data;

@Data
public class ProduitDTO {

	private Long articleId;

	private Produit produit;
	
	private MultipartFile image;
	

}
