package com.doranco.site.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commandeId;

    @Email
    @Column(nullable = false)
    private String email;

    @OneToMany(mappedBy = "commande", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<ArticleCommande> articlesCommande = new ArrayList<>();

    private LocalDate dateCommande;

    @OneToOne
    @JoinColumn(name = "paiement_id")
    private Paiement paiement;

    private Double montantTotal;
    private String statutCommande;
}
