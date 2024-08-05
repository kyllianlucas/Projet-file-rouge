package com.doranco.site.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "articles")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom de l'article est obligatoire")
    @Size(max = 100, message = "Le nom de l'article ne doit pas dépasser 100 caractères")
    @Column(name = "nom", nullable = false, length =100)
    private String name;

    @NotBlank(message = "La description est obligatoire")
    @Size(max = 500, message = "La description ne doit pas dépasser 500 caractères")
    @Column(name = "descripion", nullable = false, length = 100)
    private String description;

    @NotNull(message = "Le prix est obligatoire")
    @Min(value = 0, message = "Le prix doit être supérieur ou égal à 0")
    @Column(name = "prix", nullable = false, length = 6)
    private double prix;

    @NotNull(message = "La quantité en stock est obligatoire")
    @Min(value = 0, message = "La quantité en stock doit être supérieure ou égale à 0")
    @Column(name = "quantite", nullable = false, length = 100)
    private int quantityInStock;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sous_categorie_id")
    private SousCategorie sousCategorie;
}
