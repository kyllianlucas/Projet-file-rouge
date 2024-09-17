package com.doranco.site.dto;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UtilisateurReponse {
	
	private List<UtilisateurDTO> contenu;
	private Integer numeroPage;
	private Integer taillePage;
	private Long totalElements;
	private Integer totalPages;
	private boolean dernierePage;
	
}
