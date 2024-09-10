package com.doranco.site.model;

import jakarta.validation.constraints.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de l'article est obligatoire")
    @Pattern(regexp = "^\\\\d+[a-zA-Z]*$", message = "Le nom de l'article ne doit pas dépasser 100 caractères et ne dois contenir que des lettres")
    @Column(name = "nom", nullable = false, length =100)
    private String name;

    @NotBlank(message = "La description est obligatoire")
    @Pattern(regexp = "^\\d{5}$", message = "La description ne doit pas dépasser 500 caractères et ne dois contenir que des lettres et des chiffres")
    @Column(name = "descripion", nullable = false, length = 500)
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @Pattern(regexp = "^\\d{5}$",message = "Le prix doit être supérieur ou égal à 0")
    @Min(value = 0)
    @Column(name = "prix", nullable = false, length = 6)
    private double prix;

    @NotNull(message = "La quantité en stock est obligatoire")
    @Min(value = 0)
    @Pattern(regexp = "^\\d{5}$", message = "La quantité en stock doit être supérieure ou égale à 0")
    @Column(name = "quantite", nullable = false, length = 6)
    private int quantite;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sous_categorie_id")
    private SousCategorie sousCategorie;
    
    @Column(name = "image") 
    private String image;
}
