package com.doranco.site.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "articles_commande")
@Data
public class ArticleCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @Column(name = "quantite", nullable = false)
    private int quantite;

    @Column(name = "prix", nullable = false)
    private BigDecimal prix;
}
