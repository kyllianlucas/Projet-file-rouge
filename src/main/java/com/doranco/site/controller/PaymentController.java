package com.doranco.site.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.doranco.site.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<String> createPaymentIntent(@RequestParam double amount, @RequestParam String currency) {
        try {
            PaymentIntent paymentIntent = paymentService.createPaymentIntent(amount, currency);
            return ResponseEntity.ok(paymentIntent.getClientSecret()); // Renvoie le client_secret pour Stripe.js
        } catch (StripeException e) {
            return ResponseEntity.status(500).body("Erreur lors de la création du paiement : " + e.getMessage());
        }
    }
}
