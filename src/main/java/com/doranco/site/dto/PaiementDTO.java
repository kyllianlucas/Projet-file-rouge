package com.doranco.site.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaiementDTO {
	private Long paiementId;
	private String methodePaiement;
}
