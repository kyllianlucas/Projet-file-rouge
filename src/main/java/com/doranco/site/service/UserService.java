package com.doranco.site.service;

import com.doranco.site.model.User;
import java.util.Date;

public interface UserService {
    User registerUser(String nom, String prenom, Date dateNaissance, String email, String pseudo, String password, String telephone);
    User findByEmail(String email);
    boolean existsByEmail(String email);

}
