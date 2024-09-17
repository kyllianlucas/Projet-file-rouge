package com.doranco.site.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommandeDTO {
	
	private Long commandeId;
	private String email;
	private List<ArticleCommandeDTO> articlesCommande = new ArrayList<>();
	private LocalDate dateCommande;
	private PaiementDTO paiement;
	private Double montantTotal;
	private String statutCommande;

}
