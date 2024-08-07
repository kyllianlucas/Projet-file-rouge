package com.doranco.site.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
public class AuthService {

    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final UtilisateurRepository userRepository;
    private Map<String, String> verificationCodes = new HashMap<>();
    
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
        user.setPassword(hashPassword(password));
        user.setTelephone(telephone);
        user.setAdmin(false);
         

        Adresse adresse = new Adresse();
        adresse.setCodePostal(codePostal);
        adresse.setPays(pays);
        adresse.setRue(rue);
        adresse.setVille(ville);
        adresse.setComplementAdresse(complementAdresse);
        user.setAdresses(Collections.singleton(adresse));
       
        userRepository.save(user);
//        String code = generateVerificationCode();
//        verificationCodes.put(user.getEmail(), code);
//        emailService.sendVerificationCode(user.getEmail(), code);
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
             existingUser.setPassword(hashPassword(updatedUser.getPassword()));
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
    	Utilisateur user = findByEmail(email);
         if (user == null) {
             throw new IllegalArgumentException("Utilisateur non trouvé avec l'email : " + email);
         }

         // Encodage du nouveau mot de passe
         user.setPassword(hashPassword(newPassword));

         // Mise à jour de l'utilisateur dans la base de données
         userRepository.save(user);
    
    }

    public boolean verifyCode(String email, String code) {
    	String storedCode = verificationCodes.get(email);
        if (storedCode != null && storedCode.equals(code)) {
            verificationCodes.remove(email);
            return true;
        }
        return false;
        }

    public void sendVerificationCode(String email, String verificationCode ) {
    	emailService.sendVerificationCode(email, verificationCode);
    }
    
    public String login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        
        return "votre_jwt_token";
    }

	public Utilisateur findByEmail(String email) {
		 return userRepository.findByEmail(email);
	}
	
	 private String generateVerificationCode() {
	        // Génération d'un code à 5 chiffres
	        return String.format("%05d", new Random().nextInt(100000));
	    }
	 
	 private String hashPassword(String password) {
	        try {
	            MessageDigest digest = MessageDigest.getInstance("SHA-256");
	            byte[] hash = digest.digest(password.getBytes());
	            return Base64.getEncoder().encodeToString(hash);
	        } catch (NoSuchAlgorithmException e) {
	            throw new RuntimeException("Erreur lors du hachage du mot de passe", e);
	        }
	    }
}
