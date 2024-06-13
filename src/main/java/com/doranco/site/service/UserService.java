package com.doranco.site.service;

import com.doranco.site.model.User;
import java.util.Date;

public interface UserService {
    User findByEmail(String email);
    boolean existsByEmail(String email);
	User registerUser(String nom, String prenom, Date dateNaissance, String email, String password, String telephone,
			String pays, String codePostal, String complementAdresse, String rue, String ville);
	String login(String email, String password);
	User updateUser(Long userId, User updatedUser);
	void resetPassword(String email, String newPassword);
}
