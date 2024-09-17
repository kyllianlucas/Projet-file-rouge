	package com.doranco.site.model;
	
	import java.util.ArrayList;
	import java.util.HashSet;
	import java.util.List;
	import java.util.Set;
	
	import jakarta.persistence.CascadeType;
	import jakarta.persistence.Column;
	import jakarta.persistence.Entity;
	import jakarta.persistence.FetchType;
	import jakarta.persistence.GeneratedValue;
	import jakarta.persistence.GenerationType;
	import jakarta.persistence.Id;
	import jakarta.persistence.JoinColumn;
	import jakarta.persistence.JoinTable;
	import jakarta.persistence.ManyToMany;
	import jakarta.persistence.OneToOne;
	import jakarta.persistence.Table;
	import jakarta.validation.constraints.Email;
	import jakarta.validation.constraints.Pattern;
	import jakarta.validation.constraints.Size;
	import lombok.AllArgsConstructor;
	import lombok.Data;
	import lombok.NoArgsConstructor;
	
	@Entity
	@Table(name = "utilisateurs")
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class Utilisateur {
	
		@Id
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private Long idUtilisateur;
	
		@Size(min = 5, max = 20, message = "Le prénom doit contenir entre 5 et 20 caractères")
		@Pattern(regexp = "^[a-zA-Z]*$", message = "Le prénom ne doit pas contenir de chiffres ni de caractères spéciaux")
		private String prenom;
	
		@Size(min = 5, max = 20, message = "Le nom doit contenir entre 5 et 20 caractères")
		@Pattern(regexp = "^[a-zA-Z]*$", message = "Le nom ne doit pas contenir de chiffres ni de caractères spéciaux")
		private String nom;
	
		@Size(min = 10, max = 10, message = "Le numéro de mobile doit contenir exactement 10 chiffres")
		@Pattern(regexp = "^\\d{10}$", message = "Le numéro de mobile doit contenir uniquement des chiffres")
		private String numeroMobile;
	
		@Email
		@Column(unique = true, nullable = false)
		private String email;
	
		private String motDePasse;
	
		@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
		@JoinTable(name = "utilisateur_role", joinColumns = @JoinColumn(name = "id_utilisateur"), inverseJoinColumns = @JoinColumn(name = "id_role"))
		private Set<Role> roles = new HashSet<>();
	
		@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
		@JoinTable(name = "utilisateur_adresse", joinColumns = @JoinColumn(name = "id_utilisateur"), inverseJoinColumns = @JoinColumn(name = "id_adresse"))
		private List<Adresse> adresses = new ArrayList<>();
	
		@OneToOne(mappedBy = "utilisateur", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, orphanRemoval = true)
		private Panier panier;
	
	}
