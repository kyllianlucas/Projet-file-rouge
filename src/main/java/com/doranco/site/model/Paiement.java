package com.doranco.site.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "paiements")
@AllArgsConstructor
@NoArgsConstructor
public class Paiement {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idPaiement;

	@OneToOne(mappedBy = "paiement", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	private Commande commande ;

	@NotBlank
	@Size(min = 4, message = "Le mode de paiement doit contenir au moins 4 caractères")
	private String modePaiement;

}