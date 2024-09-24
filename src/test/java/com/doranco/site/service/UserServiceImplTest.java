//package com.doranco.site.service;
//
//import com.doranco.site.dto.*;
//import com.doranco.site.exception.APIException;
//import com.doranco.site.exception.ResourceNotFoundException;
//import com.doranco.site.model.*;
//import com.doranco.site.repository.AdresseRepository;
//import com.doranco.site.repository.RoleRepository;
//import com.doranco.site.repository.UtilisateurRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.modelmapper.ModelMapper;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import java.util.*;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//public class UserServiceImplTest {
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    @Mock
//    private UtilisateurRepository userRepo;
//
//    @Mock
//    private RoleRepository roleRepo;
//
//    @Mock
//    private AdresseRepository addressRepo;
//
//    @Mock
//    private PanierService cartService;
//
//    @Mock
//    private PasswordEncoder passwordEncoder;
//
//    @Mock
//    private ModelMapper modelMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testEnregistrerUtilisateurSuccess() {
//        // Arrange
//        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
//        utilisateurDTO.setEmail("test@example.com");
//        utilisateurDTO.setAdresse(new AdresseDTO("France", "Paris", "75001", "Rue de Rivoli", "Bâtiment A"));
//
//        Utilisateur utilisateur = new Utilisateur();
//        utilisateur.setAdresses(new ArrayList<>());
//
//        Adresse adresse = new Adresse("France", "Paris", "75001", "Rue de Rivoli", "Bâtiment A");
//        Panier panier = new Panier();
//
//        when(modelMapper.map(any(UtilisateurDTO.class), eq(Utilisateur.class))).thenReturn(utilisateur);
//        when(roleRepo.findById(anyLong())).thenReturn(Optional.of(new Role()));
//        when(addressRepo.findByCountryAndCityAndPincodeAndStreetAndBuildingName(anyString(), anyString(), anyString(), anyString(), anyString())).thenReturn(adresse);
//        when(userRepo.save(any(Utilisateur.class))).thenReturn(utilisateur);
//        when(modelMapper.map(any(Utilisateur.class), eq(UtilisateurDTO.class))).thenReturn(utilisateurDTO);
//
//        // Act
//        UtilisateurDTO result = userService.enregistrerUtilisateur(utilisateurDTO);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("test@example.com", result.getEmail());
//    }
//
//    @Test
//    void testEnregistrerUtilisateurThrowsException() {
//        // Arrange
//        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
//        utilisateurDTO.setEmail("test@example.com");
//
//        when(modelMapper.map(any(UtilisateurDTO.class), eq(Utilisateur.class))).thenThrow(new DataIntegrityViolationException(""));
//
//        // Act & Assert
//        APIException thrown = assertThrows(APIException.class, () -> {
//            userService.enregistrerUtilisateur(utilisateurDTO);
//        });
//        assertEquals("Utilisateur déjà existant avec l'email : test@example.com", thrown.getMessage());
//    }
//
//    @Test
//    void testObtenirTousLesUtilisateurs() {
//        // Arrange
//        Utilisateur utilisateur = new Utilisateur();
//        utilisateur.setIdUtilisateur(1L);
//        Adresse adresse = new Adresse();
//        adresse.setIdAdresse(1L);
//        utilisateur.setAdresses(Collections.singletonList(adresse));
//
//        Panier panier = new Panier();
//        panier.setPanierId(1L);
//        utilisateur.setPanier(panier);
//
//        List<Utilisateur> utilisateurs = Collections.singletonList(utilisateur);
//
//        // Mock des méthodes findAll et map
//        when(userRepo.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(utilisateurs));
//
//        // Initialisez le PanierDTO dans le UtilisateurDTO retourné par ModelMapper
//        PanierDTO panierDTO = new PanierDTO();  // Initialisez ici le PanierDTO
//        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
//        utilisateurDTO.setUtilisateurId(1L);
//        utilisateurDTO.setPanier(panierDTO);  // Affectez PanierDTO
//
//        when(modelMapper.map(any(Utilisateur.class), eq(UtilisateurDTO.class))).thenReturn(utilisateurDTO);
//
//        // Act
//        UtilisateurReponse response = userService.obtenirTousLesUtilisateurs(0, 10, "nom", "asc");
//
//        // Assert
//        assertNotNull(response);
//        assertFalse(response.getContenu().isEmpty());
//
//        // Ajoutez des assertions pour vérifier que PanierDTO n'est pas null
//        UtilisateurDTO dto = response.getContenu().get(0);
//        assertNotNull(dto.getPanier());  // Vérifie que PanierDTO n'est pas null
//    }
//
//
//    @Test
//    void testObtenirUtilisateurParId() {
//        // Arrange
//        Long utilisateurId = 1L;
//        Utilisateur utilisateur = new Utilisateur();
//        utilisateur.setAdresses(Collections.singletonList(new Adresse()));
//        utilisateur.setPanier(new Panier());
//
//        when(userRepo.findById(utilisateurId)).thenReturn(Optional.of(utilisateur));
//        when(modelMapper.map(any(Utilisateur.class), eq(UtilisateurDTO.class))).thenReturn(new UtilisateurDTO());
//
//        // Act
//        UtilisateurDTO result = userService.obtenirUtilisateurParId(utilisateurId);
//
//        // Assert
//        assertNotNull(result);
//    }
//
//    @Test
//    void testMettreAJourUtilisateur() {
//        // Arrange
//        Long utilisateurId = 1L;
//        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
//        utilisateurDTO.setMotDePasse("newpassword");
//
//        Utilisateur utilisateur = new Utilisateur();
//        utilisateur.setAdresses(Collections.singletonList(new Adresse()));
//
//        // Initialisation explicite du panier
//        Panier panier = new Panier();
//        utilisateur.setPanier(panier);
//
//        // Mocks
//        when(userRepo.findById(utilisateurId)).thenReturn(Optional.of(utilisateur));
//        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
//        when(userRepo.save(any(Utilisateur.class))).thenReturn(utilisateur);
//        when(modelMapper.map(any(Utilisateur.class), eq(UtilisateurDTO.class))).thenReturn(utilisateurDTO);
//
//        // Act
//        UtilisateurDTO result = userService.mettreAJourUtilisateur(utilisateurId, utilisateurDTO);
//
//        // Assert
//        assertNotNull(result);
//        assertEquals("encodedpassword", utilisateur.getMotDePasse());
//        assertNotNull(utilisateur.getPanier());  // Vérification que le panier n'est pas null
//    }
//
//
//    @Test
//    void testSupprimerUtilisateur() {
//        // Arrange
//        Long utilisateurId = 1L;
//        Utilisateur utilisateur = new Utilisateur();
//        utilisateur.setPanier(new Panier());
//
//        when(userRepo.findById(utilisateurId)).thenReturn(Optional.of(utilisateur));
//        doNothing().when(userRepo).delete(any(Utilisateur.class));
//
//        // Act
//        String result = userService.supprimerUtilisateur(utilisateurId);
//
//        // Assert
//        assertEquals("Utilisateur avec l'identifiant 1 supprimé avec succès !!!", result);
//    }
//}
//
