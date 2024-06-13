package com.doranco.site.controller;

import com.doranco.site.model.User;
import com.doranco.site.service.EmailService;
import com.doranco.site.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/users")
public class AuthController {

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public AuthController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User registeredUser = userService.registerUser(
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
        String token = userService.login(user.getEmail(), user.getPassword());
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable Long userId, @RequestBody User updatedUser) {
        User user = userService.updateUser(userId, updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        userService.resetPassword(email, newPassword);
        return new ResponseEntity<>("Mot de passe réinitialisé avec succès", HttpStatus.OK);
    }
    
    @PostMapping("/send-verification-code")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return new ResponseEntity<>("Utilisateur non trouvé avec cet email", HttpStatus.NOT_FOUND);
        }

        String verificationCode = emailService.generateRandomCode();
        emailService.sendVerificationCode(email, verificationCode);

        // Vous pouvez sauvegarder le code généré pour vérification ultérieure
        // userService.saveVerificationCode(email, verificationCode);

        return new ResponseEntity<>("Code de vérification envoyé par e-mail", HttpStatus.OK);
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@RequestParam String email, @RequestParam String code) {
        // Récupérer le code de vérification précédemment enregistré dans la base de données ou en mémoire
        // String savedCode = userService.getVerificationCode(email);

        // Comparer le code reçu avec le code enregistré
        // if (savedCode != null && savedCode.equals(code)) {
        if (code.equals("12345")) { // Simulation de vérification
            return new ResponseEntity<>("Code de vérification valide", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Code de vérification invalide", HttpStatus.BAD_REQUEST);
        }
    }
}
