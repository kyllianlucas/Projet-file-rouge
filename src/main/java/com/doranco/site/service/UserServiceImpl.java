package com.doranco.site.service;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doranco.site.dao.UserDao;
import com.doranco.site.exception.UserAlreadyExistsException;
import com.doranco.site.exception.UserNotFoundException;
import com.doranco.site.model.Adresse;
import com.doranco.site.model.Role;
import com.doranco.site.model.User;
import com.doranco.site.repository.RoleRepository;
import com.doranco.site.util.JwtUtil;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil ;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
    		JwtUtil jwtUtil,AuthenticationManager authenticationManager) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
		this.jwtUtil = jwtUtil;
		this.authenticationManager = authenticationManager;
    }

    @Override
    public User registerUser(String nom, String prenom, Date dateNaissance, String email, String password, String telephone, String pays, String codePostal, String complementAdresse, String rue, String ville) {
    	if (existsByEmail(email)) {
            throw new UserAlreadyExistsException("Un utilisateur avec cet email existe déjà.");
        }
    	User user = new User();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setDateNaissance(dateNaissance);
        user.setEmail(email);
        // Encode le mot de passe avant de l'enregistrer dans la base de données
        user.setPassword(passwordEncoder.encode(password));
        user.setTelephone(telephone);
        
        Role role = roleRepository.findByName("USER");  
        user.setRoles(Collections.singleton(role));
        
        Adresse adresse = new Adresse();
        adresse.setPays(pays);
        adresse.setVille(ville);
        adresse.setCodePostal(codePostal);
        adresse.setRue(rue);
        adresse.setComplementAdresse(complementAdresse);
        adresse.setUser(user);
               
        user.setAdresses(new HashSet<>(Collections.singletonList(adresse)));
        userDao.saveUser(user);
        return user;
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userDao.existsByEmail(email);
    }

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userDao.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Utilisateur non trouvé avec l'email : " + email);
		}
		return user;
	}
	
	public String login(String email, String password) {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (BadCredentialsException e) {
			throw new RuntimeException("Incorrect email ou mot de passe", e);
		}
		final UserDetails userDetails = loadUserByUsername(email);
		return jwtUtil.generateToken(userDetails);
	}
	
	@Override
    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userDao.findById(userId)
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

        return userDao.saveUser(existingUser);
    }
	
	@Override
    public void resetPassword(String email, String newPassword) {
        User user = userDao.findByEmail(email);
        if (user == null) {
            throw new IllegalArgumentException("Utilisateur non trouvé avec l'email : " + email);
        }

        // Encodage du nouveau mot de passe
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);

        // Mise à jour de l'utilisateur dans la base de données
        userDao.saveUser(user);
    }
}