package com.doranco.site.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor 
@NoArgsConstructor 
@Builder
public class UtilisateurDTO {

	private String nom;
	private String prenom;
	private Date dateNaissance;
	private String email;
	private String password;
	private String telephone;
}
