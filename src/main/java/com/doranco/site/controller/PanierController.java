package com.doranco.site.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doranco.site.model.Panier;
import com.doranco.site.model.Utilisateur;
import com.doranco.site.repository.UtilisateurRepository;
import com.doranco.site.service.PanierService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/Panier")
@AllArgsConstructor
public class PanierController {

    private final PanierService PanierService;
    private final UtilisateurRepository user;

    private Utilisateur getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return user.findByEmail(email);
    }

    @GetMapping
    public ResponseEntity<Panier> getPanier() {
    	Utilisateur user = getCurrentUser();
        Panier Panier = PanierService.getPanier(user);
        return new ResponseEntity<>(Panier, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Panier> addToPanier(@RequestParam Long articleId, @RequestParam int quantity) {
    	Utilisateur user = getCurrentUser();
        Panier Panier = PanierService.addToPanier(user, articleId, quantity);
        return new ResponseEntity<>(Panier, HttpStatus.OK);
    }

    @DeleteMapping("/remove/{PanierItemId}")
    public ResponseEntity<Panier> removeFromPanier(@PathVariable Long PanierItemId) {
    	Utilisateur user = getCurrentUser();
        Panier Panier = PanierService.removeFromPanier(user, PanierItemId);
        return new ResponseEntity<>(Panier, HttpStatus.OK);
    }

    @PostMapping("/clear")
    public ResponseEntity<Void> clearPanier() {
    	Utilisateur user = getCurrentUser();
        PanierService.clearPanier(user);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
