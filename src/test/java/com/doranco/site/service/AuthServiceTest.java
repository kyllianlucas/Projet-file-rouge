package com.doranco.site.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.doranco.site.exception.EmailException;
import com.doranco.site.exception.UserNotFoundException;
import com.doranco.site.model.Adresse;
import com.doranco.site.model.Utilisateur;
import com.doranco.site.repository.UtilisateurRepository;

public class AuthServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UtilisateurRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(new Utilisateur());

        // Act & Assert
        EmailException exception = assertThrows(EmailException.class, () -> {
            authService.registerUser("nom", "prenom", new Date(), email, "password", "telephone", 
                                     "pays", "codePostal", "complementAdresse", "rue", "ville");
        });
        assertEquals("Un utilisateur avec cet email existe déjà.", exception.getMessage());
    }

    @Test
    void testRegisterUser_Success() throws EmailException {
        // Arrange
        String email = "test@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        Utilisateur savedUser = new Utilisateur();
        when(userRepository.save(any(Utilisateur.class))).thenReturn(savedUser);

        // Act
        Utilisateur result = authService.registerUser("nom", "prenom", new Date(), email, "password", "telephone", 
                                                      "pays", "codePostal", "complementAdresse", "rue", "ville");

        // Assert
        assertNotNull(result);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class, () -> {
            authService.updateUser(userId, new Utilisateur());
        });
        assertEquals("Utilisateur non trouvé avec l'id : " + userId, exception.getMessage());
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        Long userId = 1L;
        Utilisateur existingUser = new Utilisateur();
        existingUser.setId(userId);
        existingUser.setNom("ancienNom");

        Adresse existingAdresse = new Adresse();
        existingAdresse.setId(1L);
        existingUser.setAdresses(Collections.singleton(existingAdresse));

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(Utilisateur.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Utilisateur updatedUser = new Utilisateur();
        updatedUser.setNom("nouveauNom");
        Adresse updatedAdresse = new Adresse();
        updatedAdresse.setId(1L);
        updatedAdresse.setRue("nouvelleRue");
        updatedUser.setAdresses(Collections.singleton(updatedAdresse));

        // Act
        Utilisateur result = authService.updateUser(userId, updatedUser);

        // Assert
        assertNotNull(result);
        assertEquals("nouveauNom", result.getNom());
        assertEquals("nouvelleRue", result.getAdresses().iterator().next().getRue());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testLogin_Success() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        // Act
        String result = authService.login(email, password);

        // Assert
        assertNotNull(result);
        assertEquals("votre_jwt_token", result);
    }
}
