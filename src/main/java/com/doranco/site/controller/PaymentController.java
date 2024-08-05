package com.doranco.site.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doranco.site.model.Panier;
import com.doranco.site.model.Utilisateur;
import com.doranco.site.repository.UtilisateurRepository;
import com.doranco.site.service.CommandeService;
import com.doranco.site.service.PanierService;
import com.doranco.site.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@AllArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PanierService panierService;
    private final CommandeService commandeService;
    private final UtilisateurRepository userRepository; // Inject UserRepository instead of UserService

    @PostMapping("/create-payment-intent")
    public ResponseEntity<Map<String, String>> createPaymentIntent(@RequestParam double amount) {
        try {
            PaymentIntent paymentIntent = paymentService.createPaymentIntent(amount, "eur");

            Map<String, String> response = new HashMap<>();
            response.put("clientSecret", paymentIntent.getClientSecret());

            return ResponseEntity.ok(response);
        } catch (StripeException e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @PostMapping("/confirm-payment")
    public ResponseEntity<String> confirmPayment(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Récupérer l'utilisateur à partir des détails de l'authentification
        	Utilisateur user = userRepository.findByEmail(userDetails.getUsername()); // Use UserRepository
            
            // Récupérer le panier de l'utilisateur
            Panier panier = panierService.getPanier(user);

            // Convertir le panier en commande
            commandeService.createCommandeFromPanier(panier);

            return ResponseEntity.ok("Paiement confirmé et commande créée");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
