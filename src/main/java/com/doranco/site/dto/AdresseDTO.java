package com.doranco.site.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class AdresseDTO {
	 
	private String codePostal;
	 private String pays;
	 private String complementAdresse;
	 private String rue;
	 private String ville;
	
}
