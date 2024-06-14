package com.doranco.site.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doranco.site.exception.UserNotFoundException;
import com.doranco.site.model.Adresse;
import com.doranco.site.model.User;
import com.doranco.site.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserServiceImpl implements UserService, UserDetailsService {

   
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Override
    public User registerUser(User toSave) {        
        User user = userRepository.save(toSave);
//        sendVerificationCode(user.getEmail());
              
        return user;
    }

    @Override
    public User findByEmail(String email) {
    	return userRepository.findByEmail(email).orElse(null);

    }



	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email);
		}
		return user;
	}
	
	@Override
    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
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
	
	@Override
    public void resetPassword(String email, String newPassword) {
        User user = findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'email : " + email);
        }

        // Encodage du nouveau mot de passe
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        // Mise à jour de l'utilisateur dans la base de données
        userRepository.save(user);
    }

	@Override
	public void sendVerificationCode(String email) {
		String verificationCode = emailService.generateVerificationCode();
        emailService.sendVerificationCode(email, verificationCode);
	}
}