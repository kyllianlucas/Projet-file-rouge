package com.doranco.site.model;

import javax.validation.constraints.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "adresse")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Adresse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Le pays est requis")
    @Size(max = 100, message = "Le pays ne doit pas dépasser 100 caractères")
    @Pattern(regexp = "^[a-zA-ZàáâäãåąčćęèéêëěìíîïłńòóôöõøùúûüůýÿżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËĚÌÍÎÏŁŃÒÓÔÖÕØÙÚÛÜŮÝŸŻŹÑÇČŠŽ ]*$",
        message = "Le pays ne doit contenir que des lettres")
    @Column(name = "pays", nullable = false, length = 100)
    private String pays;

    @NotBlank(message = "La ville est requise")
    @Size(max = 100, message = "La ville ne doit pas dépasser 100 caractères")
    @Pattern(regexp = "^[a-zA-ZàáâäãåąčćęèéêëěìíîïłńòóôöõøùúûüůýÿżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËĚÌÍÎÏŁŃÒÓÔÖÕØÙÚÛÜŮÝŸŻŹÑÇČŠŽ ]*$",
        message = "La ville ne doit contenir que des lettres")
    @Column(name = "ville", nullable = false, length = 100)
    private String ville;
    
    @NotBlank(message = "Le numéro de rue est requis")
    @Pattern(regexp = "^\\d+[a-zA-Z]*$", message = "Le numéro de rue doit commencer par un ou plusieurs chiffres, suivi éventuellement d'une lettre")
    @Column(name = "rue", nullable = false, length = 255)
    private String rue;
    
    @NotBlank(message = "Le code postal est requis")
    @Pattern(regexp = "^\\d{5}$", message = "Le code postal doit être composé de 5 chiffres")
    @Column(name = "code_postal",nullable = false, length = 5)
    private String codePostal;
    
    @Size(max = 100, message = "Le complément d'adresse ne doit pas dépasser 100 caractères")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$", message = "Le complément d'adresse ne doit contenir que des lettres, des chiffres et des espaces")
    @Column(name = "complement", nullable = true, length = 100)
    private String complementAdresse;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;
}
