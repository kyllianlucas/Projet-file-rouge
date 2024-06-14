package com.doranco.site.service;

import com.doranco.site.model.User;

public interface UserService {
    User findByEmail(String email);
    User registerUser(User user);
    User updateUser(Long userId, User updatedUser);
    void resetPassword(String email, String newPassword);
    void sendVerificationCode(String email);
}
