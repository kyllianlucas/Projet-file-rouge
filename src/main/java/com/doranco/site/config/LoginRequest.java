package com.doranco.site.config;

import lombok.Data;

@Data
public class LoginRequest {
	private String email;
    private String password;
}
