	package com.doranco.site.model;
	
	import jakarta.persistence.Entity;
	import jakarta.persistence.GeneratedValue;
	import jakarta.persistence.GenerationType;
	import jakarta.persistence.Id;
	import jakarta.persistence.JoinColumn;
	import jakarta.persistence.ManyToOne;
	import jakarta.persistence.Table;
	import lombok.AllArgsConstructor;
	import lombok.Data;
	import lombok.NoArgsConstructor;
	
	@Entity
	@Data
	@Table(name = "articles_de_commande")
	@AllArgsConstructor
	@NoArgsConstructor
	public class ArticleCommande {
		
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long idArticleDeCommande;
		
		@ManyToOne
		@JoinColumn(name = "id_article")
		private Article article;
		
		@ManyToOne
		@JoinColumn(name = "id_panier")
		private Commande commande;
		
		private Integer quantite;
		private double remise;
		private double prixProduitCommande;
		
	}