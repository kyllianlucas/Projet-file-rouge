package com.doranco.site.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "adresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Adresse {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idAdresse;

	@NotBlank
	@Size(min = 5, message = "Le nom de la rue doit contenir au moins 5 caractères")
	private String street;
	
	@NotBlank
	@Size(min = 5, message = "Le nom du bâtiment doit contenir au moins 5 caractères")
	private String buildingName;
	
	@NotBlank
	@Size(min = 4, message = "Le nom de la ville doit contenir au moins 4 caractères")
	private String city;
	
	
	@NotBlank
	@Size(min = 2, message = "Le nom du pays doit contenir au moins 2 caractères")
	private String country;
	
	@NotBlank
	@Size(min = 6, message = "Le code postal doit contenir au moins 6 caractères")
	private String pincode;

	@ManyToMany(mappedBy = "adresses")
	private List<Utilisateur> utilisateurs = new ArrayList<>();

	public Adresse(String country, String city, String pincode, String street, String buildingName) {
		this.country = country;
		this.city = city;
		this.pincode = pincode;
		this.street = street;
		this.buildingName = buildingName;
	}

}
