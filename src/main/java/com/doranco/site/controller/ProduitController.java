package com.doranco.site.controller;

import com.doranco.site.model.Produit;
import com.doranco.site.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    // Endpoint pour afficher le formulaire d'ajout de produit
    @GetMapping("/produits/add")
    public String showAddProduitForm(Model model) {
        model.addAttribute("produit", new Produit());
        return "add_produit";
    }

    // Endpoint pour traiter le formulaire d'ajout de produit
    @PostMapping("/produits/add")
    public String addProduit(@ModelAttribute("produit") Produit produit) {
        produitService.saveProduit(produit);
        return "redirect:/produits";
    }
    
    @PostMapping("/produits/{id}/quantite")
    public String updateProduitQuantite(@PathVariable("id") Long produitId, @RequestParam("quantite") int nouvelleQuantite) {
        produitService.updateQuantite(produitId, nouvelleQuantite);
        return "redirect:/produits";
    }
    
    @DeleteMapping("/{id}")
    public String deleteProduit(@PathVariable("id") Long produitId) {
        produitService.deleteProduit(produitId);
        return "redirect:/produits";
    }

}
