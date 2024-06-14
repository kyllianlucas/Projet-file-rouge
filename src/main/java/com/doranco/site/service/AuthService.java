package com.doranco.site.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doranco.site.exception.EmailException;
import com.doranco.site.model.Adresse;
import com.doranco.site.model.Role;
import com.doranco.site.model.User;
import com.doranco.site.repository.RoleRepository;
import com.doranco.site.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Transactional
public class AuthService {

    private final UserService userService;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private Map<String, String> verificationCodes = new HashMap<>();
    
    public User registerUser(String nom, String prenom, Date dateNaissance, String email, String password, String telephone,
                             String pays, String codePostal, String complementAdresse, String rue, String ville) throws EmailException {
        User userExist = userService.findByEmail(email);
    	if (userExist != null) {
            throw new EmailException("Un utilisateur avec cet email existe déjà.");
        }

        User user = new User();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setDateNaissance(dateNaissance);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setTelephone(telephone);

        Role role = roleRepository.findByName("USER");  
        user.setRoles(Collections.singleton(role));

        Adresse adresse = new Adresse();
        adresse.setCodePostal(codePostal);
        adresse.setPays(pays);
        adresse.setRue(rue);
        adresse.setVille(ville);
        adresse.setComplementAdresse(complementAdresse);
        user.setAdresses(Collections.singleton(adresse));
       
        userRepository.save(user);
        String code = generateVerificationCode();
        verificationCodes.put(user.getEmail(), code);
        emailService.sendVerificationCode(user.getEmail(), code);
        return user;
    }

    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userService.findByEmail(updatedUser.getEmail());
        if (existingUser == null) {
            throw new RuntimeException("Utilisateur non trouvé avec l'email : " + updatedUser.getEmail());
        }

        // Mettre à jour les champs individuellement s'ils sont fournis
        if (updatedUser.getNom() != null) {
            existingUser.setNom(updatedUser.getNom());
        }
        if (updatedUser.getPrenom() != null) {
            existingUser.setPrenom(updatedUser.getPrenom());
        }
        if (updatedUser.getTelephone() != null) {
            existingUser.setTelephone(updatedUser.getTelephone());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword());
        }
        if (updatedUser.getDateNaissance() != null) {
            existingUser.setDateNaissance(updatedUser.getDateNaissance());
        }

        // Vous pouvez ajouter la logique pour mettre à jour les adresses ici si nécessaire

        return userService.updateUser(userId, existingUser);
    }

    public void resetPassword(String email, String newPassword) {
        userService.resetPassword(email, newPassword);
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

	public User findByEmail(String email) {
		 return userService.findByEmail(email);
	}
	
	 private String generateVerificationCode() {
	        // Génération d'un code à 5 chiffres
	        return String.format("%05d", new Random().nextInt(100000));
	    }
}
