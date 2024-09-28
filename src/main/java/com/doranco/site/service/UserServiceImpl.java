package com.doranco.site.service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doranco.site.config.AppConfig;
import com.doranco.site.dto.AdresseDTO;
import com.doranco.site.dto.ArticleDTO;
import com.doranco.site.dto.PanierDTO;
import com.doranco.site.dto.UtilisateurDTO;
import com.doranco.site.dto.UtilisateurReponse;
import com.doranco.site.exception.APIException;
import com.doranco.site.exception.ResourceNotFoundException;
import com.doranco.site.model.Adresse;
import com.doranco.site.model.ArticlePanier;
import com.doranco.site.model.Panier;
import com.doranco.site.model.Role;
import com.doranco.site.model.Utilisateur;
import com.doranco.site.repository.AdresseRepository;
import com.doranco.site.repository.RoleRepository;
import com.doranco.site.repository.UtilisateurRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UtilisateurRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    private AdresseRepository addressRepo;

    @Autowired
    private PanierService cartService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UtilisateurDTO enregistrerUtilisateur(UtilisateurDTO utilisateurDTO) {

        try {
            // Mapping DTO to entity
            Utilisateur utilisateur = modelMapper.map(utilisateurDTO, Utilisateur.class);
            
            if (utilisateur.getRoles() == null) {
                utilisateur.setRoles(new HashSet<>()); // Utiliser HashSet pour initialiser les rôles
            }
            
            // Ajout du rôle par défaut (utilisateur)
            Role role = roleRepo.findById(AppConfig.ID_UTILISATEUR).get();
            utilisateur.getRoles().add(role);

            // Gestion de l'adresse de l'utilisateur
            String pays = utilisateurDTO.getAdresse().getPays();
            String ville = utilisateurDTO.getAdresse().getVille();
            String codePostal = utilisateurDTO.getAdresse().getCodePostal();
            String rue = utilisateurDTO.getAdresse().getRue();
            String nomBatiment = utilisateurDTO.getAdresse().getNomBatiment();

            // Vérification si l'adresse existe déjà, sinon création
            Adresse adresse = addressRepo.findByCountryAndCityAndPincodeAndStreetAndBuildingName(pays, 
                    ville, codePostal, rue, nomBatiment);

            if (adresse == null) {
                adresse = new Adresse(pays, ville, codePostal, rue, nomBatiment);
                adresse = addressRepo.save(adresse);
            }

            utilisateur.setAdresses(List.of(adresse));

            // Si un panier est fourni dans le DTO, l'associer à l'utilisateur
            if (utilisateurDTO.getPanier() != null) {
                Panier panier = modelMapper.map(utilisateurDTO.getPanier(), Panier.class);
                utilisateur.setPanier(panier);
                panier.setUtilisateur(utilisateur);  // Associer le panier à l'utilisateur
            }

            // Sauvegarde de l'utilisateur (avec ou sans panier)
            Utilisateur utilisateurEnregistre = userRepo.save(utilisateur);

            // Mapping de l'utilisateur enregistré vers le DTO de réponse
            utilisateurDTO = modelMapper.map(utilisateurEnregistre, UtilisateurDTO.class);
            utilisateurDTO.setAdresse(modelMapper.map(utilisateur.getAdresses().stream().findFirst().get(), AdresseDTO.class));

            // Si un panier est associé, le mapper également dans le DTO
            if (utilisateurEnregistre.getPanier() != null) {
                utilisateurDTO.setPanier(modelMapper.map(utilisateurEnregistre.getPanier(), PanierDTO.class));
            }

            return utilisateurDTO;

        } catch (DataIntegrityViolationException e) {
            throw new APIException("Utilisateur déjà existant avec l'email : " + utilisateurDTO.getEmail());
        }
    }


    @Override
    public UtilisateurReponse obtenirTousLesUtilisateurs(Integer numeroPage, Integer taillePage, String trierPar, String ordreTrier) {
        Sort tri = ordreTrier.equalsIgnoreCase("asc") ? Sort.by(trierPar).ascending()
                : Sort.by(trierPar).descending();

        Pageable detailsPage = PageRequest.of(numeroPage, taillePage, tri);

        Page<Utilisateur> pageUtilisateurs = userRepo.findAll(detailsPage);

        List<Utilisateur> utilisateurs = pageUtilisateurs.getContent();

        if (utilisateurs.size() == 0) {
            throw new APIException("Aucun utilisateur n'existe !!!");
        }

        List<UtilisateurDTO> utilisateursDTO = utilisateurs.stream().map(utilisateur -> {
            UtilisateurDTO dto = modelMapper.map(utilisateur, UtilisateurDTO.class);

            if (utilisateur.getAdresses().size() != 0) {
                dto.setAdresse(modelMapper.map(utilisateur.getAdresses().stream().findFirst().get(), AdresseDTO.class));
            }

            PanierDTO panier = modelMapper.map(utilisateur.getPanier(), PanierDTO.class);

            List<ArticleDTO> produits = utilisateur.getPanier().getItemsPanier().stream()
                .map(item -> modelMapper.map(item.getProduit(), ArticleDTO.class)).collect(Collectors.toList());

            dto.setPanier(panier);
            dto.getPanier().setArticles(produits);

            return dto;

        }).collect(Collectors.toList());

        UtilisateurReponse réponseUtilisateur = new UtilisateurReponse();

        réponseUtilisateur.setContenu(utilisateursDTO);
        réponseUtilisateur.setNumeroPage(pageUtilisateurs.getNumber());
        réponseUtilisateur.setTaillePage(pageUtilisateurs.getSize());
        réponseUtilisateur.setTotalElements(pageUtilisateurs.getTotalElements());
        réponseUtilisateur.setTotalPages(pageUtilisateurs.getTotalPages());
        réponseUtilisateur.setDernierePage(pageUtilisateurs.isLast());

        return réponseUtilisateur;
    }

    @Override
    public UtilisateurDTO obtenirUtilisateurParId(Long utilisateurId) {
        Utilisateur utilisateur = userRepo.findById(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "utilisateurId", utilisateurId));

        UtilisateurDTO utilisateurDTO = modelMapper.map(utilisateur, UtilisateurDTO.class);

        utilisateurDTO.setAdresse(modelMapper.map(utilisateur.getAdresses().stream().findFirst().get(), AdresseDTO.class));

        PanierDTO panier = modelMapper.map(utilisateur.getPanier(), PanierDTO.class);

        List<ArticleDTO> produits = utilisateur.getPanier().getItemsPanier().stream()
                .map(item -> modelMapper.map(item.getProduit(), ArticleDTO.class)).collect(Collectors.toList());

        utilisateurDTO.setPanier(panier);
        utilisateurDTO.getPanier().setArticles(produits);

        return utilisateurDTO;
    }

    @Override
    public UtilisateurDTO mettreAJourUtilisateur(UtilisateurDTO utilisateurDTO) {
        // Récupérer l'utilisateur existant à partir de la base de données par email
        String email = utilisateurDTO.getEmail();
        Utilisateur utilisateur = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", email));

        // Mise à jour des informations de l'utilisateur avec les données fournies dans le DTO
        if (utilisateurDTO.getPrenom() != null && !utilisateurDTO.getPrenom().trim().isEmpty()) {
            utilisateur.setPrenom(utilisateurDTO.getPrenom());
        }

        if (utilisateurDTO.getNom() != null && !utilisateurDTO.getNom().trim().isEmpty()) {
            utilisateur.setNom(utilisateurDTO.getNom());
        }

        if (utilisateurDTO.getNumeroMobile() != null && !utilisateurDTO.getNumeroMobile().trim().isEmpty()) {
            utilisateur.setNumeroMobile(utilisateurDTO.getNumeroMobile());
        }

        if (utilisateurDTO.getEmail() != null && !utilisateurDTO.getEmail().trim().isEmpty()) {
            utilisateur.setEmail(utilisateurDTO.getEmail());
        }

        if (utilisateurDTO.getMotDePasse() != null && !utilisateurDTO.getMotDePasse().trim().isEmpty()) {
            utilisateur.setMotDePasse(passwordEncoder.encode(utilisateurDTO.getMotDePasse()));
        }

        // Mise à jour de l'adresse (si elle est fournie)
        if (utilisateurDTO.getAdresse() != null) {
            AdresseDTO adresseDTO = utilisateurDTO.getAdresse();
            Adresse adresse = utilisateur.getAdresses().isEmpty() ? new Adresse() : utilisateur.getAdresses().get(0);

            if (adresseDTO.getPays() != null && !adresseDTO.getPays().trim().isEmpty()) {
                adresse.setCountry(adresseDTO.getPays());
            }

            if (adresseDTO.getVille() != null && !adresseDTO.getVille().trim().isEmpty()) {
                adresse.setCity(adresseDTO.getVille());
            }

            if (adresseDTO.getCodePostal() != null && !adresseDTO.getCodePostal().trim().isEmpty()) {
                adresse.setPincode(adresseDTO.getCodePostal());
            }

            if (adresseDTO.getRue() != null && !adresseDTO.getRue().trim().isEmpty()) {
                adresse.setStreet(adresseDTO.getRue());
            }

            if (adresseDTO.getNomBatiment() != null && !adresseDTO.getNomBatiment().trim().isEmpty()) {
                adresse.setBuildingName(adresseDTO.getNomBatiment());
            }

            // Enregistrer l'adresse mise à jour dans la base de données
            if (adresse.getIdAdresse() == null) { // Si l'adresse est nouvelle
                adresse = addressRepo.save(adresse);
                utilisateur.setAdresses(List.of(adresse)); // Associer l'adresse à l'utilisateur
            }
        }

        // Sauvegarde de l'utilisateur mis à jour
        userRepo.save(utilisateur);

        // Mapper l'entité Utilisateur mise à jour vers un UtilisateurDTO
        UtilisateurDTO updatedUtilisateurDTO = modelMapper.map(utilisateur, UtilisateurDTO.class);

        // Si l'adresse existe, la mapper dans le DTO
        if (utilisateur.getAdresses() != null && !utilisateur.getAdresses().isEmpty()) {
            updatedUtilisateurDTO.setAdresse(modelMapper.map(utilisateur.getAdresses().get(0), AdresseDTO.class));
        }

        // Retourner l'utilisateur mis à jour
        return updatedUtilisateurDTO;
    }




    @Override
    public String supprimerUtilisateur(Long utilisateurId) {
        Utilisateur utilisateur = userRepo.findById(utilisateurId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "utilisateurId", utilisateurId));

        List<ArticlePanier> articlesPanier = utilisateur.getPanier().getItemsPanier();
        Long panierId = utilisateur.getPanier().getPanierId();

        articlesPanier.forEach(item -> {
            Long produitId = item.getProduit().getIdProduit();
            cartService.supprimerArticleDuPanier(panierId, produitId);
        });

        userRepo.delete(utilisateur);

        return "Utilisateur avec l'identifiant " + utilisateurId + " supprimé avec succès !!!";
    }
}

