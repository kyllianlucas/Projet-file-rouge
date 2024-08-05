package com.doranco.site.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doranco.site.model.Commande;
import com.doranco.site.model.Item;
import com.doranco.site.model.Panier;
import com.doranco.site.repository.CommandeRepository;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    public Commande createCommandeFromPanier(Panier panier) {
        Commande commande = new Commande();
        commande.setDateCommande(new Date());

        for (Item panierItem : panier.getItems()) {
            Item commandeItem = new Item();
            commandeItem.setArticle(panierItem.getArticle());
            commandeItem.setQuantity(panierItem.getQuantity());
            commandeItem.setCommande(commande);
            commande.getItems().add(commandeItem);
        }

        return commandeRepository.save(commande);
    }
}
