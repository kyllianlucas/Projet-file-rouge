package com.doranco.site.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import com.doranco.site.exception.EmailException;
import com.doranco.site.model.Utilisateur;
import com.doranco.site.repository.UtilisateurRepository;

public class AuthServiceTest {

	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UtilisateurRepository userRepository;
	
	private Utilisateur utilisateur;
	
	@Autowired
	private AuthService authService;
	
	@Before
    public void setUp() {
        // Mock existing user
		utilisateur = new Utilisateur();
		utilisateur.setId(1L);
		utilisateur.setEmail("existing@example.com");
		Mockito.when(userRepository.findByEmail(utilisateur.getEmail())).thenReturn(utilisateur);

        // Mock save user
        Mockito.when(userRepository.save(any(Utilisateur.class))).thenReturn(utilisateur);
    }
	
	@Test
	public void testRegisterUser_Success() throws EmailException {
	
		// Given
        String nom = "John";
        String prenom = "Doe";
        Date dateNaissance = new Date();
        String email = "john.doe@example.com";
        String password = "password";
        String telephone = "1234567890";
        String pays = "France";
        String codePostal = "75001";
        String complementAdresse = "Apt 123";
        String rue = "Rue de Rivoli";
        String ville = "Paris";

        // When
        Utilisateur newUser = authService.registerUser(nom, prenom, dateNaissance, email, password, telephone,
                pays, codePostal, complementAdresse, rue, ville);

        // Then
        assertEquals(email, newUser.getEmail());
        assertEquals(nom, newUser.getNom());
        assertEquals(prenom, newUser.getPrenom());
        assertEquals(dateNaissance, newUser.getDateNaissance());
        assertEquals(telephone, newUser.getTelephone());
        assertEquals(false, newUser.isAdmin()); // Vérifie que l'utilisateur n'est pas admin
        assertEquals(1, newUser.getAdresses().size()); // Vérifie que l'adresse est correctement ajoutée

        // Verify interactions with mocks
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).save(any(Utilisateur.class));
        verify(emailService, times(1)).sendVerificationCode(eq(email), anyString());
    }
}
