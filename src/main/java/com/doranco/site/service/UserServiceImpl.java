package com.doranco.site.service;

import java.util.Collections;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.doranco.site.dao.UserDao;
import com.doranco.site.exception.UserAlreadyExistsException;
import com.doranco.site.model.Role;
import com.doranco.site.model.User;
import com.doranco.site.repository.RoleRepository;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserDao userDao, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
		this.roleRepository = roleRepository;
    }

    @Override
    public User registerUser(String nom, String prenom, Date dateNaissance, String email, String pseudo, String password, String telephone) {
    	if (existsByEmail(email)) {
            throw new UserAlreadyExistsException("Un utilisateur avec cet email existe déjà.");
        }
    	User user = new User();
        user.setNom(nom);
        user.setPrenom(prenom);
        user.setDateNaissance(dateNaissance);
        user.setEmail(email);
        user.setPseudo(pseudo);
        // Encode le mot de passe avant de l'enregistrer dans la base de données
        user.setPassword(passwordEncoder.encode(password));
        user.setTelephone(telephone);
        
        Role role = roleRepository.findByName("USER");  
        user.setRoles(Collections.singleton(role));
               
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
}
