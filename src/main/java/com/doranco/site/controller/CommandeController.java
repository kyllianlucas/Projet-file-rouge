package com.doranco.site.controller;

import com.doranco.site.model.Commande;
import com.doranco.site.service.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/commandes")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @GetMapping("/list")
    public String getAllCommandes(Model model) {
        List<Commande> commandes = commandeService.getAllCommandes();
        model.addAttribute("commandes", commandes);
        return "commandes/list";
    }

    @GetMapping("/detail/{id}")
    public String getCommandeDetail(@PathVariable("id") Long id, Model model) {
        Commande commande = commandeService.getCommandeById(id).orElse(null);
        if (commande != null) {
            model.addAttribute("commande", commande);
            return "commandes/detail";
        } else {
            return "redirect:/commandes/list";
        }
    }

    @GetMapping("/create")
    public String showCreateCommandeForm(Model model) {
        model.addAttribute("commande", new Commande());
        return "commandes/create";
    }

    @PostMapping("/create")
    public String createCommande(@ModelAttribute("commande") Commande commande) {
        commandeService.saveCommande(commande);
        return "redirect:/commandes/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCommande(@PathVariable("id") Long id) {
        commandeService.deleteCommande(id);
        return "redirect:/commandes/list";
    }
}
