package com.doranco.site.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doranco.site.exception.EmailException;
import com.doranco.site.model.User;
import com.doranco.site.model.VerificationRequest;
import com.doranco.site.service.AuthService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class AuthController {

	private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws EmailException {
        User registeredUser = authService.registerUser(
                user.getNom(),
                user.getPrenom(),
                user.getDateNaissance(),
                user.getEmail(),
                user.getPassword(),
                user.getTelephone(),
                user.getAdresses().iterator().next().getPays(),
                user.getAdresses().iterator().next().getCodePostal(),
                user.getAdresses().iterator().next().getComplementAdresse(),
                user.getAdresses().iterator().next().getRue(),
                user.getAdresses().iterator().next().getVille()
        );
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user) {
        String token = authService.login(user.getEmail(), user.getPassword());
        if (token != null) {
            return new ResponseEntity<>(token, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Identifiants incorrects", HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        User user = authService.updateUser(userId, updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
    	authService.resetPassword(email, newPassword);
        return new ResponseEntity<>("Mot de passe réinitialisé avec succès", HttpStatus.OK);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestBody VerificationRequest verificationRequest) {
        
    	 boolean isValid = authService.verifyCode(verificationRequest.getEmail(), verificationRequest.getVerificationCode());
         if (isValid) {
             return ResponseEntity.ok("Code vérifié avec succès");
         } else {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Code de vérification invalide");
         }
    }
}
