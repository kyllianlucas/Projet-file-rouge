package com.doranco.site.service;

import com.doranco.site.model.Adresse;
import com.doranco.site.model.User;
import com.doranco.site.repository.AdresseRepository;
import com.doranco.site.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AdresseService {

    private final AdresseRepository adresseRepository;
    private final UserRepository userRepository;

    public AdresseService(AdresseRepository adresseRepository, UserRepository userRepository) {
        this.adresseRepository = adresseRepository;
        this.userRepository = userRepository;
    }

    public Optional<Adresse> getAdresseById(Long id) {
        return adresseRepository.findById(id);
    }

    public Adresse saveAdresse(Adresse adresse, Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            adresse.setUser(userOptional.get());
            return adresseRepository.save(adresse);
        } else {
            throw new IllegalArgumentException("User not found: " + userId);
        }
    }

    public void deleteAdresse(Long id) {
        adresseRepository.deleteById(id);
    }
}
