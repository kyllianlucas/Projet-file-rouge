package com.doranco.site.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public String generateRandomCode() {
        Random random = new Random();
        int code = 10000 + random.nextInt(90000); // Génère un code entre 10000 et 99999
        return String.valueOf(code);
    }

    public void sendVerificationCode(String email, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Code de vérification");
        message.setText("Votre code de vérification est : " + code);
        javaMailSender.send(message);
    }

    public String generateVerificationCode() {
        return generateRandomCode(); // Utilisation de generateRandomCode() pour générer un code de vérification
    }
}
