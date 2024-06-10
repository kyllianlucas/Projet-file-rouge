package com.doranco.site.service;

import com.doranco.site.model.Commande;
import com.doranco.site.repository.CommandeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class CommandeServiceTest {

    @Mock
    private CommandeRepository commandeRepository;

    @InjectMocks
    private CommandeService commandeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllCommandes() {
        List<Commande> commandes = new ArrayList<>();
        commandes.add(new Commande());
        commandes.add(new Commande());

        when(commandeRepository.findAll()).thenReturn(commandes);

        List<Commande> result = commandeService.getAllCommandes();

        assertEquals(2, result.size());
    }

    @Test
    void testGetCommandeById() {
        Commande commande = new Commande();
        commande.setId(1L);

        when(commandeRepository.findById(1L)).thenReturn(Optional.of(commande));

        Optional<Commande> result = commandeService.getCommandeById(1L);

        assertEquals(commande.getId(), result.get().getId());
    }

    @Test
    void testSaveCommande() {
        Commande commande = new Commande();
        commande.setId(1L);

        when(commandeRepository.save(commande)).thenReturn(commande);

        Commande result = commandeService.saveCommande(commande);

        assertEquals(commande.getId(), result.getId());
    }

    @Test
    void testDeleteCommande() {
        Long id = 1L;

        commandeService.deleteCommande(id);

        verify(commandeRepository, times(1)).deleteById(id);
    }
}
