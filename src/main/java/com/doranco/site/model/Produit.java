package com.doranco.site.model;

import java.util.ArrayList;
import java.util.List;

import com.doranco.site.util.StringListConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "produits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idProduit;

    @NotBlank(message = "Le nom du produit doit contenir au moins 3 caractères")
    @Size(min = 3)
    @NotNull // Champ requis
    private String productName;
    
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] image;
    
    @NotBlank(message = "La description du produit doit contenir au moins 6 caractères")
    @Size(min = 6, max = 2000)
    @Column(length = 2000)
    @NotNull // Champ requis
    private String description;
    
    @NotNull(message = "Le prix doit être spécifié")
    private double prix = 0.0;
    
    @NotNull(message = "La remise doit être spécifiée")
    private double remise = 0.0;
    
    @NotNull(message = "La quantité doit être spécifiée")
    private Integer quantite;
    
    @NotNull(message = "Le prix spécial doit être spécifié")
    private double prixSpecial = 0.0;

    @ManyToOne
    @JoinColumn(name = "id_categorie")
    @NotNull(message = "La catégorie doit être spécifiée")
    private Categorie categorie;
    
    @OneToMany(mappedBy = "produit", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    private List<ArticlePanier> produitDuPanier = new ArrayList<>();
    
    @OneToMany(mappedBy = "produit", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<ArticleCommande> produitDeCommande = new ArrayList<>();

    // Sous-catégorie pour les vetements (homme, femme, enfant)
    private String sousCategorie;

    private String genre;
    
    // Taille (pour les produits comme vetement) - pas besoin d'annotation NotNull ici
    @Convert(converter = StringListConverter.class)
    private List<String> taille; // Peut être null
    
    
    @Convert(converter = StringListConverter.class)
    private List<String> pointure;
    
    private String marque;
}
