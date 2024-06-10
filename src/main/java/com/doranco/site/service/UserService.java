package com.doranco.site.service;

import com.doranco.site.model.User;
import com.doranco.site.model.Role;
import com.doranco.site.repository.RoleRepository;
import com.doranco.site.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public User saveUser(User user, String roleName) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Role> userRoleOptional = roleRepository.findByName(roleName);
        if (userRoleOptional.isPresent()) {
            Role userRole = userRoleOptional.get();
            user.setRoles(Set.of(userRole));
            return userRepository.save(user);
        } else {
            throw new IllegalArgumentException("Role not found: " + roleName);
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
