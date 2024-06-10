package com.doranco.site.model;

import java.math.BigDecimal;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "produits")
@Data
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "prix", nullable = false)
    private BigDecimal prix;

    @Column(name = "quantite_stock", nullable = false)
    private int quantiteStock;

    @Column(name = "marque" , nullable = false)
    private String marque;
    
    @Column (name = "disponible", nullable = false)
    private boolean disponible;
    
    @ManyToMany
    @JoinTable(
        name = "produit_categorie",
        joinColumns = @JoinColumn(name = "produit_id"),
        inverseJoinColumns = @JoinColumn(name = "categorie_id")
    )
    private Set<Categorie> categories;
}
