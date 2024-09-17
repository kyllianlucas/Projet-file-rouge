package com.doranco.site.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class UtilisateurDTO {

	private Long utilisateurId;
	private String prenom;
	private String nom;
	private String numeroMobile;
	private String email;
	private String motDePasse;
	private AdresseDTO adresse;
	private PanierDTO panier;
	private Set<String> roles;
}
