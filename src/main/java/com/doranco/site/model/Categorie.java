package com.doranco.site.model;

import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "categories")
@Data
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nom", nullable = false, unique = true)
    private String nom;

    @ManyToMany(mappedBy = "categories")
    private Set<Produit> produits;
}
