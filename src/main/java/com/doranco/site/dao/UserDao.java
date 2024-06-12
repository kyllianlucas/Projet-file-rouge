package com.doranco.site.dao;

import com.doranco.site.model.User;

public interface UserDao {
    void saveUser(User user);
    User findByEmail(String email);
    boolean existsByEmail(String email);
}
