package com.doranco.site.dao;

import java.util.Optional;

import com.doranco.site.model.User;

public interface UserDao {
    User saveUser(User user);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findById(Long userId);
}
