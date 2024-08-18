package com.doranco.site.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.doranco.site.dto.AdresseDTO;
import com.doranco.site.dto.UtilisateurDTO;
import com.doranco.site.exception.EmailException;
import com.doranco.site.exception.UserNotFoundException;
import com.doranco.site.model.Utilisateur;
import com.doranco.site.repository.UtilisateurRepository;

class AuthServiceTest {

    @Mock
    private UtilisateurRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_Success() throws EmailException {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setNom("John");
        utilisateurDTO.setPrenom("Doe");
        utilisateurDTO.setEmail("johndoe@example.com");
        utilisateurDTO.setPassword("password");
        utilisateurDTO.setTelephone("123456789");

        AdresseDTO adresseDTO = new AdresseDTO();
        adresseDTO.setCodePostal("75001");
        adresseDTO.setPays("France");
        adresseDTO.setRue("123 rue de Paris");
        adresseDTO.setVille("Paris");

        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(null);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        Utilisateur result = authService.registerUser(utilisateurDTO, adresseDTO);

        assertNotNull(result);
        assertEquals("John", result.getNom());
        assertEquals("Doe", result.getPrenom());
        assertEquals("johndoe@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());
        verify(userRepository, times(1)).save(result);
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setEmail("johndoe@example.com");

        Utilisateur existingUser = new Utilisateur();
        when(userRepository.findByEmail("johndoe@example.com")).thenReturn(existingUser);

        assertThrows(EmailException.class, () -> {
            authService.registerUser(utilisateurDTO, new AdresseDTO());
        });

        verify(userRepository, never()).save(any(Utilisateur.class));
    }

    @Test
    void testUpdateUser_Success() {
        Long userId = 1L;
        Utilisateur existingUser = new Utilisateur();
        existingUser.setId(userId);
        existingUser.setNom("John");

        Utilisateur updatedUser = new Utilisateur();
        updatedUser.setNom("Jane");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        Utilisateur result = authService.updateUser(userId, updatedUser);

        assertEquals("Jane", result.getNom());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        Long userId = 1L;
        Utilisateur updatedUser = new Utilisateur();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            authService.updateUser(userId, updatedUser);
        });

        verify(userRepository, never()).save(any(Utilisateur.class));
    }

    @Test
    void testResetPassword_Success() {
        String email = "johndoe@example.com";
        String newPassword = "newPassword";
        Utilisateur existingUser = new Utilisateur();
        existingUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(existingUser);
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");

        authService.resetPassword(email, newPassword);

        assertEquals("encodedNewPassword", existingUser.getPassword());
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testResetPassword_UserNotFound() {
        String email = "johndoe@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.resetPassword(email, "newPassword");
        });

        verify(userRepository, never()).save(any(Utilisateur.class));
    }

    @Test
    void testLoadUserByUsername_UserFound() {
        String email = "johndoe@example.com";
        Utilisateur existingUser = new Utilisateur();
        existingUser.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(existingUser);

        UserDetails userDetails = authService.loadUserByUsername(email);

        assertNotNull(userDetails);
        assertEquals(email, userDetails.getUsername());
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        String email = "johndoe@example.com";
        when(userRepository.findByEmail(email)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.loadUserByUsername(email);
        });
    }
}
