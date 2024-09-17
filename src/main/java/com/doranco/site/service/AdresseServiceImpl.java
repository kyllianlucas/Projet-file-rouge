package com.doranco.site.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.doranco.site.repository.AdresseRepository;
import com.doranco.site.repository.UtilisateurRepository;
import com.doranco.site.model.Adresse;
import com.doranco.site.model.Utilisateur;
import com.doranco.site.dto.AdresseDTO;
import com.doranco.site.exception.APIException;
import com.doranco.site.exception.ResourceNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdresseServiceImpl implements AdresseService {

    @Autowired
    private AdresseRepository adresseRepo;

    @Autowired
    private UtilisateurRepository utilisateurRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public AdresseDTO créerAdresse(AdresseDTO adresseDTO) {

        String pays = adresseDTO.getPays();
        String ville = adresseDTO.getVille();
        String codePostal = adresseDTO.getCodePostal();
        String rue = adresseDTO.getRue();
        String nomBâtiment = adresseDTO.getNomBatiment();

        Adresse adresseDepuisDB = adresseRepo.findByCountryAndCityAndPincodeAndStreetAndBuildingName(pays,
                 ville, codePostal, rue, nomBâtiment);

        if (adresseDepuisDB != null) {
            throw new APIException("Adresse déjà existante avec l'adresseId: " + adresseDepuisDB.getIdAdresse());
        }

        Adresse adresse = modelMapper.map(adresseDTO, Adresse.class);

        Adresse adresseEnregistrée = adresseRepo.save(adresse);

        return modelMapper.map(adresseEnregistrée, AdresseDTO.class);
    }

    @Override
    public List<AdresseDTO> obtenirAdresses() {
        List<Adresse> adresses = adresseRepo.findAll();

        List<AdresseDTO> adresseDTOs = adresses.stream().map(adresse -> modelMapper.map(adresse, AdresseDTO.class))
                .collect(Collectors.toList());

        return adresseDTOs;
    }

    @Override
    public AdresseDTO obtenirAdresse(Long adresseId) {
        Adresse adresse = adresseRepo.findById(adresseId)
                .orElseThrow(() -> new ResourceNotFoundException("Adresse", "adresseId", adresseId));

        return modelMapper.map(adresse, AdresseDTO.class);
    }

    @Override
    public AdresseDTO mettreÀJourAdresse(Long adresseId, Adresse adresse) {
        Adresse adresseDepuisDB = adresseRepo.findByCountryAndCityAndPincodeAndStreetAndBuildingName(
                adresse.getCountry(),  adresse.getCity(), adresse.getPincode(), adresse.getStreet(),
                adresse.getBuildingName());

        if (adresseDepuisDB == null) {
            adresseDepuisDB = adresseRepo.findById(adresseId)
                    .orElseThrow(() -> new ResourceNotFoundException("Adresse", "adresseId", adresseId));

            adresseDepuisDB.setCountry(adresse.getCountry());
            adresseDepuisDB.setCity(adresse.getCity());
            adresseDepuisDB.setPincode(adresse.getPincode());
            adresseDepuisDB.setStreet(adresse.getStreet());
            adresseDepuisDB.setBuildingName(adresse.getBuildingName());

            Adresse adresseMiseÀJour = adresseRepo.save(adresseDepuisDB);

            return modelMapper.map(adresseMiseÀJour, AdresseDTO.class);
        } else {
            List<Utilisateur> utilisateurs = utilisateurRepo.findByAdresses(adresseId);
            final Adresse a = adresseDepuisDB;

            utilisateurs.forEach(utilisateur -> utilisateur.getAdresses().add(a));

            supprimerAdresse(adresseId);

            return modelMapper.map(adresseDepuisDB, AdresseDTO.class);
        }
    }

    @Override
    public String supprimerAdresse(Long adresseId) {
        Adresse adresseDepuisDB = adresseRepo.findById(adresseId)
                .orElseThrow(() -> new ResourceNotFoundException("Adresse", "adresseId", adresseId));

        List<Utilisateur> utilisateurs = utilisateurRepo.findByAdresses(adresseId);

        utilisateurs.forEach(utilisateur -> {
            utilisateur.getAdresses().remove(adresseDepuisDB);

            utilisateurRepo.save(utilisateur);
        });

        adresseRepo.deleteById(adresseId);

        return "Adresse supprimée avec succès avec l'adresseId: " + adresseId;
    }
}

