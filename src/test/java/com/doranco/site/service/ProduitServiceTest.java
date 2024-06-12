package com.doranco.site.service;

import com.doranco.site.exception.ResourceNotFoundException;
import com.doranco.site.model.Produit;
import com.doranco.site.repository.ProduitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProduitServiceTest {

    @Mock
    private ProduitRepository produitRepository;

    @InjectMocks
    private ProduitService produitService;

    private Produit produit;

    @BeforeEach
    void setUp() {
        produit = new Produit();
        produit.setId(1L);
        produit.setNom("Produit test");
        produit.setDescription("Description du produit test");
        produit.setPrix(new BigDecimal("25.50"));
        produit.setQuantiteStock(10);
        produit.setMarque("Marque test");
        produit.setDisponible(true);
    }

    @Test
    void testFindById() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        Produit produitFound = produitService.findById(1L);
        assertNotNull(produitFound);
        assertEquals("Produit test", produitFound.getNom());
    }

    @Test
    void testFindById_NotFound() {
        when(produitRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> produitService.findById(1L));
    }

    @Test
    void testAddProduit() {
        when(produitRepository.save(produit)).thenReturn(produit);
        Produit produitAdded = produitService.addProduit(produit);
        assertNotNull(produitAdded);
        assertEquals("Marque test", produitAdded.getMarque());
    }

    @Test
    void testUpdateQuantite() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(produitRepository.save(produit)).thenReturn(produit); // Mock the save operation

        Produit produitMisAJour = produitService.updateQuantite(1L, 5);

        assertNotNull(produitMisAJour);
        assertEquals(5, produitMisAJour.getQuantiteStock());
        assertTrue(produitMisAJour.isDisponible());
    }

    @Test
    void testUpdateQuantiteZero() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        when(produitRepository.save(produit)).thenReturn(produit); // Mock the save operation

        Produit produitMisAJour = produitService.updateQuantite(1L, 0);

        assertNotNull(produitMisAJour);
        assertEquals(0, produitMisAJour.getQuantiteStock());
        assertFalse(produitMisAJour.isDisponible());
    }

    @Test
    void testDeleteProduit() {
        produitService.deleteProduit(1L);
        verify(produitRepository, times(1)).deleteById(1L);
    }
}
