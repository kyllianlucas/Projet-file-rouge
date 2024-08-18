package com.doranco.site.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doranco.site.dto.AdresseDTO;
import com.doranco.site.dto.UtilisateurDTO;
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
    
    
    /**
     * Inscrit un nouvel utilisateur avec les informations fournies.
     *
     * @param utilisateurDTO les données de l'utilisateur à inscrire.
     * @param adresseDTO les données de l'adresse associée à l'utilisateur.
     * @return l'objet {@link Utilisateur} représentant l'utilisateur inscrit.
     * @throws EmailException si un utilisateur avec le même email existe déjà.
     */
    public Utilisateur registerUser(UtilisateurDTO utilisateurDTO ,AdresseDTO adresseDto) throws EmailException {
    	Utilisateur userExist = userRepository.findByEmail(utilisateurDTO.getEmail());
    	if (userExist != null) {
            throw new EmailException("Un utilisateur avec cet email existe déjà.");
        }

    	Utilisateur user = new Utilisateur();
        user.setNom(utilisateurDTO.getNom());
        user.setPrenom(utilisateurDTO.getPrenom());
        user.setDateNaissance(utilisateurDTO.getDateNaissance());
        user.setEmail(utilisateurDTO.getEmail());
        user.setPassword(passwordEncoder.encode(utilisateurDTO.getPassword()));
        user.setTelephone(utilisateurDTO.getTelephone());
        user.setAdmin(false);
        
        
        Adresse adresse = new Adresse();
        adresse.setCodePostal(adresseDto.getCodePostal());
        adresse.setPays(adresseDto.getPays());
        adresse.setRue(adresseDto.getRue());
        adresse.setVille(adresseDto.getVille());
        adresse.setComplementAdresse(adresseDto.getComplementAdresse());    
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