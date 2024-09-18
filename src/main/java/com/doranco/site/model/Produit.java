package com.doranco.site.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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

	@NotBlank
	@Size(min = 3, message = "Le nom du produit doit contenir au moins 3 caractères")
	private String productName;
	
	private String image;
	
	@NotBlank
	@Size(min = 6, message = "La description du produit doit contenir au moins 6 caractères")
	private String description;
	
	private Integer quantite;
	private double prix;
	private double remise;
	private double prixSpecial;

	@ManyToOne
	@JoinColumn(name = "id_categorie")
	private Categorie categorie;
	
	@OneToMany(mappedBy = "produit", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
	private List<ArticlePanier> produitDuPanier = new ArrayList<>();
	
	@OneToMany(mappedBy = "produit", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private List<ArticleCommande> produitDeCommande = new ArrayList<>();

	// Sous-catégorie pour les tee-shirts (homme, femme, enfant)
    private String sousCategorie;

    // Taille (pour les produits comme tee-shirt)
    private List<String> taille;
}

