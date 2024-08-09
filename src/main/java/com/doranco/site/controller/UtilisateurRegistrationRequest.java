package com.doranco.site.controller;

import lombok.Data;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Data
public class UtilisateurRegistrationRequest {

    @NotBlank(message = "Le nom est requis")
    private String nom;

    @NotBlank(message = "Le prénom est requis")
    private String prenom;

    @NotNull(message = "La date de naissance est requise")
    private Date dateNaissance;

    @NotBlank(message = "L'email est requis")
    @Email(message = "L'email doit être valide")
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    @NotBlank(message = "Le numéro de téléphone est requis")
    private String telephone;

    @NotBlank(message = "Le pays est requis")
    private String pays;

    @NotBlank(message = "Le code postal est requis")
    @Pattern(regexp = "^\\d{5}$", message = "Le code postal doit être composé de 5 chiffres")
    private String codePostal;

    private String complementAdresse;

    @NotBlank(message = "Le numéro de rue est requis")
    private String rue;

    @NotBlank(message = "La ville est requise")
    private String ville;
}
