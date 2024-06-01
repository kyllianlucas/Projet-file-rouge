package com.doranco.site.model;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.*;
import javax.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
@Table(name = "utilisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Le nom est requis")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    @Pattern(regexp = "^[a-zA-ZàáâäãåąčćęèéêëěìíîïłńòóôöõøùúûüůýÿżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËĚÌÍÎÏŁŃÒÓÔÖÕØÙÚÛÜŮÝŸŻŹÑÇČŠŽ ]*$", 
        message = "Le nom ne doit contenir que des lettres")
    @Column(name = "nom", nullable = false)
    private String nom;

    @NotBlank(message = "Le prénom est requis")
    @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
    @Pattern(regexp = "^[a-zA-ZàáâäãåąčćęèéêëěìíîïłńòóôöõøùúûüůýÿżźñçčšžÀÁÂÄÃÅĄĆČĖĘÈÉÊËĚÌÍÎÏŁŃÒÓÔÖÕØÙÚÛÜŮÝŸŻŹÑÇČŠŽ ]*$", 
        message = "Le prénom ne doit contenir que des lettres")
    @Column(name = "prenom", nullable = false)
    private String prenom;

    @NotBlank(message = "L'email est requis")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.(com|fr)$", 
        message = "L'email doit être valide et se terminer par .com ou .fr")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Le mot de passe est requis")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
        message = "Le mot de passe doit contenir au moins 8 caractères, une lettre majuscule, une lettre minuscule, un chiffre et un caractère spécial")
    @Column(name = "password", nullable = false)
    private String password;

    @NotBlank(message = "Le numéro de téléphone est requis")
    @Pattern(regexp="(^$|[0-9]{10})", message = "Le numéro de téléphone doit contenir 10 chiffres")
    @Column(name = "telephone", nullable = false)
    private String telephone;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_naissance")
    private Date dateNaissance;

    @OneToMany(mappedBy = "user")
    private Set<Commande> commandes;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Adresse> adresses;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
            .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
}
