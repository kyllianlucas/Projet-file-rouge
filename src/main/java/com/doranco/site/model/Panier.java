package com.doranco.site.model;


import java.math.BigDecimal;
import java.util.Set;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "paniers")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Panier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "utilisateur_id", referencedColumnName = "id")
    private User utilisateur;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL)
    private Set<Produit> items;

    @Column(name = "total")
    private BigDecimal total;
}
