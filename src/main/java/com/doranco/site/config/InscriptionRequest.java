package com.doranco.site.config;

import com.doranco.site.dto.AdresseDTO;
import com.doranco.site.dto.UtilisateurDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InscriptionRequest {
	
	private UtilisateurDTO utilisateurDTO;
	
	private AdresseDTO adresseDTO;

    private String captchaToken;  
    
    private Boolean isAdmin;
}
