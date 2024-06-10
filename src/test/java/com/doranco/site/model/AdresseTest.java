package com.doranco.site.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AdresseTest {

    private Adresse adresse;

    @BeforeEach
    public void setUp() {
        adresse = new Adresse();
        adresse.setId(1L);
        adresse.setPays("France");
        adresse.setVille("Paris");
        adresse.setRue("123 Rue de la Paix");
        adresse.setCodePostal("75000");
        adresse.setComplementAdresse("Appartement 45B");
    }

    @Test
    public void testGetPays() {
        assertEquals("France", adresse.getPays());
    }

    @Test
    public void testGetVille() {
        assertEquals("Paris", adresse.getVille());
    }

    @Test
    public void testGetRue() {
        assertEquals("123 Rue de la Paix", adresse.getRue());
    }

    @Test
    public void testGetCodePostal() {
        assertEquals("75000", adresse.getCodePostal());
    }

    @Test
    public void testGetComplementAdresse() {
        assertEquals("Appartement 45B", adresse.getComplementAdresse());
    }
}
