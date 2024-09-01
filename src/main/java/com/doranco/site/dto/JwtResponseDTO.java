package com.doranco.site.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class JwtResponseDTO {
	private String accessToken;
	private boolean isAdmin;
}

