package com.doranco.site.dto;

import lombok.Data;

@Data
public class JWTAuthRequest {
	private String username;  // email
	private String motDePasse;
}