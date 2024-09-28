package com.doranco.site.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.doranco.site.config.AppConfig;
import com.doranco.site.dto.AdresseDTO;
import com.doranco.site.dto.PanierDTO;
import com.doranco.site.dto.UtilisateurDTO;
import com.doranco.site.dto.UtilisateurReponse;
import com.doranco.site.exception.APIException;
import com.doranco.site.exception.ResourceNotFoundException;
import com.doranco.site.model.Adresse;
import com.doranco.site.model.Panier;
import com.doranco.site.model.Role;
import com.doranco.site.model.Utilisateur;
import com.doranco.site.repository.AdresseRepository;
import com.doranco.site.repository.RoleRepository;
import com.doranco.site.repository.UtilisateurRepository;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UtilisateurRepository userRepo;

    @Mock
    private RoleRepository roleRepo;

    @Mock
    private AdresseRepository addressRepo;

    @Mock
    private PanierService cartService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private UtilisateurDTO utilisateurDTO;
    private Utilisateur utilisateur;
    private Adresse adresse;
    private Panier panier;
    private Role role;

    @BeforeEach
    public void setUp() {
        // Initialisation des objets Utilisateur, Adresse, et Panier pour les tests
        utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setEmail("test@example.com");
        utilisateurDTO.setPrenom("John");
        utilisateurDTO.setNom("Doe");
        
        // Initialisation du PanierDTO
        PanierDTO panierDTO = new PanierDTO();
        panierDTO.setArticles(new ArrayList<>()); // Initialisation de la liste d'articles
        utilisateurDTO.setPanier(panierDTO); // Assignation à UtilisateurDTO

        utilisateur = new Utilisateur();
        utilisateur.setEmail("test@example.com");
        utilisateur.setPrenom("John");
        utilisateur.setNom("Doe");
        utilisateur.setAdresses(Collections.singletonList(new Adresse()));
        utilisateur.setPanier(new Panier()); // Panier associé à l'utilisateur

        adresse = new Adresse();
        adresse.setCountry("France");
        adresse.setCity("Paris");
        adresse.setPincode("75001");
        adresse.setStreet("Rue de Rivoli");
        adresse.setBuildingName("Batiment A");

        panier = new Panier();
        panier.setUtilisateur(utilisateur);

        role = new Role();
        role.setIdRole(AppConfig.ID_UTILISATEUR);
        role.setNomRole("ROLE_UTILISATEUR");
    }


//    @Test
//    public void testEnregistrerUtilisateur_Success() {
//        // Configuration des mocks
//        when(modelMapper.map(any(), eq(Utilisateur.class))).thenReturn(utilisateur);
//        when(roleRepo.findById(AppConfig.ID_UTILISATEUR)).thenReturn(Optional.of(role));
//        when(addressRepo.findByCountryAndCityAndPincodeAndStreetAndBuildingName(any(), any(), any(), any(), any()))
//                .thenReturn(null); // Adresse n'existe pas
//        when(addressRepo.save(any(Adresse.class))).thenReturn(adresse);
//        when(userRepo.save(any(Utilisateur.class))).thenReturn(utilisateur);
//        when(modelMapper.map(utilisateur, UtilisateurDTO.class)).thenReturn(utilisateurDTO);
//
//        // Appel de la méthode à tester
//        UtilisateurDTO result = userService.enregistrerUtilisateur(utilisateurDTO);
//
//        // Assertions
//        assertNotNull(result);
//        assertEquals("test@example.com", result.getEmail());
//        verify(userRepo, times(1)).save(any(Utilisateur.class));
//    }

//    @Test
//    public void testEnregistrerUtilisateur_UtilisateurExist() {
//        // Configuration des mocks pour simuler un utilisateur existant
//        when(modelMapper.map(any(), eq(Utilisateur.class))).thenReturn(utilisateur);
//        when(roleRepo.findById(AppConfig.ID_UTILISATEUR)).thenReturn(Optional.of(role));
//        when(addressRepo.findByCountryAndCityAndPincodeAndStreetAndBuildingName(any(), any(), any(), any(), any()))
//                .thenReturn(null); // Adresse n'existe pas
//        when(addressRepo.save(any(Adresse.class))).thenReturn(adresse);
//        when(userRepo.save(any(Utilisateur.class))).thenThrow(new DataIntegrityViolationException(""));
//
//        // Appel de la méthode à tester et vérification de l'exception
//        APIException exception = assertThrows(APIException.class, () -> {
//            userService.enregistrerUtilisateur(utilisateurDTO);
//        });
//
//        assertEquals("Utilisateur déjà existant avec l'email : test@example.com", exception.getMessage());
//        verify(userRepo, times(1)).save(any(Utilisateur.class));
//    }

//    @Test
//    public void testObtenirTousLesUtilisateurs_Success() {
//        // Configuration des mocks
//        List<Utilisateur> utilisateursList = new ArrayList<>(Collections.singletonList(utilisateur));
//        when(userRepo.findAll(any())).thenReturn(new PageImpl<>(utilisateursList));
//        when(modelMapper.map(utilisateur, UtilisateurDTO.class)).thenReturn(utilisateurDTO);
//
//        UtilisateurReponse response = userService.obtenirTousLesUtilisateurs(0, 10, "nom", "asc");
//
//        assertNotNull(response);
//        assertEquals(1, response.getContenu().size());
//        assertEquals("test@example.com", response.getContenu().get(0).getEmail());
//    }

//    @Test
//    public void testObtenirTousLesUtilisateurs_NoUsers() {
//        // Configuration des mocks
//        when(userRepo.findAll(any())).thenReturn(new PageImpl<>(new ArrayList<>()));
//
//        APIException exception = assertThrows(APIException.class, () -> {
//            userService.obtenirTousLesUtilisateurs(0, 10, "nom", "asc");
//        });
//
//        assertEquals("Aucun utilisateur n'existe !!!", exception.getMessage());
//    }

//    @Test
//    public void testObtenirUtilisateurParId_Success() {
//        // Configuration des mocks
//        when(userRepo.findById(anyLong())).thenReturn(Optional.of(utilisateur));
//        when(modelMapper.map(utilisateur, UtilisateurDTO.class)).thenReturn(utilisateurDTO);
//
//        UtilisateurDTO result = userService.obtenirUtilisateurParId(1L);
//
//        assertNotNull(result);
//        assertEquals("test@example.com", result.getEmail());
//    }

    @Test
    public void testObtenirUtilisateurParId_NotFound() {
        // Configuration des mocks
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.obtenirUtilisateurParId(1L);
        });

        assertEquals("Utilisateur not found with utilisateurId: 1", exception.getMessage());
    }

    @Test
    public void testMettreAJourUtilisateur_Success() {
        // Configuration des mocks
        when(userRepo.findByEmail(any())).thenReturn(Optional.of(utilisateur));
        when(userRepo.save(any())).thenReturn(utilisateur);
        when(modelMapper.map(utilisateur, UtilisateurDTO.class)).thenReturn(utilisateurDTO);

        UtilisateurDTO updatedUser = userService.mettreAJourUtilisateur(utilisateurDTO);

        assertNotNull(updatedUser);
        assertEquals("test@example.com", updatedUser.getEmail());
    }

    @Test
    public void testMettreAJourUtilisateur_NotFound() {
        // Configuration des mocks
        when(userRepo.findByEmail(any())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.mettreAJourUtilisateur(utilisateurDTO);
        });

        assertEquals("Utilisateur not found with email: test@example.com", exception.getMessage());
    }

//    @Test
//    public void testSupprimerUtilisateur_Success() {
//        // Configuration des mocks
//        when(userRepo.findById(anyLong())).thenReturn(Optional.of(utilisateur));
//        Mockito.when(userRepo.delete(any())).thenReturn(null);
//
//        String result = userService.supprimerUtilisateur(1L);
//        assertEquals("Utilisateur avec l'identifiant 1 supprimé avec succès !!!", result);
//    }

    @Test
    public void testSupprimerUtilisateur_NotFound() {
        // Configuration des mocks
        when(userRepo.findById(anyLong())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.supprimerUtilisateur(1L);
        });

        assertEquals("Utilisateur not found with utilisateurId: 1", exception.getMessage());
    }
}
