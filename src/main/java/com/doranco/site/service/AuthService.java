package com.doranco.site.service;

import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doranco.site.config.SecurityConfig;
import com.doranco.site.exception.EmailException;
import com.doranco.site.exception.UserNotFoundException;
import com.doranco.site.model.Adresse;
import com.doranco.site.model.Utilisateur;
import com.doranco.site.repository.UtilisateurRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthService implements UserDetailsService {

    @Autowired
    private final UtilisateurRepository userRepository;
    @Autowired
    private final PasswordEncoder passwordEncoder;
    
    
    public Utilisateur registerUser(String nom, String prenom, Date dateNaissance, String email, String password, String telephone,
                             String pays, String codePostal, String complementAdresse, String rue, String ville) throws EmailException {
    	Utilisateur userExist = userRepository.findByEmail(email);
    	if (userExist != null) {
            throw new EmailException("Un utilisateur avec cet email existe déjà.");
        }

    	Utilisateur user = new Utilisateur();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setDateNaissance(dateNaissance);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setTelephone(telephone);
        user.setAdmin(false);
        
        
        Adresse adresse = new Adresse();
        adresse.setCodePostal(codePostal);
        adresse.setPays(pays);
        adresse.setRue(rue);
        adresse.setVille(ville);
        adresse.setComplementAdresse(complementAdresse);    
        user.getAdresses().add(adresse);
        adresse.setUser(user);
        userRepository.save(user);
        return user;
    }

    public Utilisateur updateUser(Long userId, Utilisateur updatedUser) {
    	Utilisateur existingUser = userRepository.findById(userId)
                 .orElseThrow(() -> new UserNotFoundException("Utilisateur non trouvé avec l'id : " + userId));

         // Mettre à jour les champs individuellement s'ils sont fournis
         if (updatedUser.getNom() != null) {
             existingUser.setNom(updatedUser.getNom());
         }
         if (updatedUser.getPrenom() != null) {
             existingUser.setPrenom(updatedUser.getPrenom());
         }
         if (updatedUser.getEmail() != null) {
             existingUser.setEmail(updatedUser.getEmail());
         }
         if (updatedUser.getTelephone() != null) {
             existingUser.setTelephone(updatedUser.getTelephone());
         }
         if (updatedUser.getPassword() != null) {
             existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
         }

         if (updatedUser.getAdresses() != null && !updatedUser.getAdresses().isEmpty()) {
             Adresse updatedAdresse = updatedUser.getAdresses().iterator().next(); // Suppose que seule une adresse est mise à jour
             Adresse existingAdresse = existingUser.getAdresses().stream()
                     .filter(a -> a.getId().equals(updatedAdresse.getId()))
                     .findFirst()
                     .orElse(null);
             
             if (existingAdresse != null) {
                 existingAdresse.setPays(updatedAdresse.getPays());
                 existingAdresse.setVille(updatedAdresse.getVille());
                 existingAdresse.setCodePostal(updatedAdresse.getCodePostal());
                 existingAdresse.setRue(updatedAdresse.getRue());
                 existingAdresse.setComplementAdresse(updatedAdresse.getComplementAdresse());
             } else {
                 throw new RuntimeException("Adresse non trouvée pour l'utilisateur avec ID : " + userId);
             }
         }

         return userRepository.save(existingUser);
     }

    public void resetPassword(String email, String newPassword) {
    	Utilisateur user = (Utilisateur) loadUserByUsername(email);
         if (user == null) {
             throw new IllegalArgumentException("Utilisateur non trouvé avec l'email : " + email);
         }

         // Encodage du nouveau mot de passe
         user.setPassword(passwordEncoder.encode(newPassword));

         // Mise à jour de l'utilisateur dans la base de données
         userRepository.save(user);
    
    }
   
    @Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	Utilisateur user = userRepository.findByEmail(email);
		if (user == null) {
	        throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email);
	    }
	    return user;
	}
}