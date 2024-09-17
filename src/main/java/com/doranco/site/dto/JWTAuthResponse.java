package com.doranco.site.dto;

import lombok.Data;

@Data
public class JWTAuthResponse {
	private String token;
	
	private UtilisateurDTO utilisateur;
}