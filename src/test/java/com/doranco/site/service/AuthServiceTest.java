package com.doranco.site.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.doranco.site.exception.EmailException;
import com.doranco.site.exception.UserNotFoundException;
import com.doranco.site.model.Adresse;
import com.doranco.site.model.Utilisateur;
import com.doranco.site.repository.UtilisateurRepository;


@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UtilisateurRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private Utilisateur user;

    @BeforeEach
    void setUp() {
        user = new Utilisateur();
        user.setId(1L);
        user.setNom("Doe");
        user.setPrenom("John");
        user.setEmail("john.doe@example.com");
        user.setPassword("password");
        user.setTelephone("123456789");
        user.setDateNaissance(new Date());
        user.setAdmin(false);

        Adresse adresse = new Adresse();
        adresse.setCodePostal("75000");
        adresse.setPays("France");
        adresse.setRue("123 Rue Example");
        adresse.setVille("Paris");
        user.setAdresses(Collections.singleton(adresse));
    }

    @Test
    void testRegisterUserSuccess() throws EmailException {
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(Utilisateur.class))).thenReturn(user);

        Utilisateur savedUser = authService.registerUser(user.getNom(), user.getPrenom(), user.getDateNaissance(),
                user.getEmail(), user.getPassword(), user.getTelephone(), "France", "75000", "Complement",
                "Rue Example", "Paris");

        assertNotNull(savedUser);
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals("hashedPassword", savedUser.getPassword());
        verify(userRepository, times(1)).save(any(Utilisateur.class));
    }

    @Test
    void testRegisterUserEmailExists() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        assertThrows(EmailException.class, () -> {
            authService.registerUser(user.getNom(), user.getPrenom(), user.getDateNaissance(), user.getEmail(),
                    user.getPassword(), user.getTelephone(), "France", "75000", "Complement", "Rue Example", "Paris");
        });

        verify(userRepository, never()).save(any(Utilisateur.class));
    }

    @Test
    void testUpdateUserSuccess() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("newHashedPassword");
        when(userRepository.save(any(Utilisateur.class))).thenReturn(user);

        Utilisateur updatedUser = new Utilisateur();
        updatedUser.setNom("UpdatedName");
        updatedUser.setPassword("newPassword");

        Utilisateur result = authService.updateUser(1L, updatedUser);

        assertNotNull(result);
        assertEquals("UpdatedName", result.getNom());
        assertEquals("newHashedPassword", result.getPassword());
        verify(userRepository, times(1)).save(any(Utilisateur.class));
    }

    @Test
    void testUpdateUserNotFound() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            authService.updateUser(1L, new Utilisateur());
        });

        verify(userRepository, never()).save(any(Utilisateur.class));
    }

    @Test
    void testResetPasswordSuccess() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(passwordEncoder.encode(anyString())).thenReturn("newHashedPassword");

        authService.resetPassword("john.doe@example.com", "newPassword");

        verify(userRepository, times(1)).save(user);
        assertEquals("newHashedPassword", user.getPassword());
    }

    @Test
    void testResetPasswordUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.resetPassword("nonexistent@example.com", "newPassword");
        });

        verify(userRepository, never()).save(any(Utilisateur.class));
    }

    @Test
    void testLoadUserByUsernameSuccess() {
        when(userRepository.findByEmail(anyString())).thenReturn(user);

        UserDetails foundUser = authService.loadUserByUsername("john.doe@example.com");

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getUsername());
    }

    @Test
    void testLoadUserByUsernameNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            authService.loadUserByUsername("nonexistent@example.com");
        });
    }
}
