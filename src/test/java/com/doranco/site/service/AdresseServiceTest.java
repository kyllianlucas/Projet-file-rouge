package com.doranco.site.service;

import com.doranco.site.model.Adresse;
import com.doranco.site.model.User;
import com.doranco.site.repository.AdresseRepository;
import com.doranco.site.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdresseServiceTest {

    @Mock
    private AdresseRepository adresseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdresseService adresseService;

    private Adresse adresse;
    private User user;

    @BeforeEach
    public void setUp() {
        adresse = new Adresse();
        adresse.setId(1L);
        adresse.setPays("France");
        adresse.setVille("Paris");
        adresse.setRue("Rue de Rivoli");
        adresse.setCodePostal("75001");

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        adresse.setUser(user);
    }

    @Test
    public void testGetAdresseById() {
        when(adresseRepository.findById(1L)).thenReturn(Optional.of(adresse));

        Optional<Adresse> result = adresseService.getAdresseById(1L);

        assertTrue(result.isPresent());
        assertEquals(adresse, result.get());
        verify(adresseRepository, times(1)).findById(1L);
    }

    @Test
    public void testSaveAdresse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(adresseRepository.save(any(Adresse.class))).thenReturn(adresse);

        Adresse savedAdresse = adresseService.saveAdresse(adresse, 1L);

        assertNotNull(savedAdresse);
        assertEquals(adresse, savedAdresse);
        verify(userRepository, times(1)).findById(1L);
        verify(adresseRepository, times(1)).save(adresse);
    }

    @Test
    public void testSaveAdresseUserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            adresseService.saveAdresse(adresse, 1L);
        });

        assertEquals("User not found: 1", exception.getMessage());
        verify(userRepository, times(1)).findById(1L);
        verify(adresseRepository, times(0)).save(any(Adresse.class));
    }

    @Test
    public void testDeleteAdresse() {
        doNothing().when(adresseRepository).deleteById(1L);

        adresseService.deleteAdresse(1L);

        verify(adresseRepository, times(1)).deleteById(1L);
    }
}
