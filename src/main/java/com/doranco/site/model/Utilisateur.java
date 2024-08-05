package com.doranco.site.model;

import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "utilisateurs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Utilisateur implements UserDetails {
    
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
    private Set<Adresse> adresses;

    @Column(name = "is_admin", nullable = false)
    private boolean isAdmin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    	return isAdmin ? Set.of(new SimpleGrantedAuthority("ROLE_ADMIN")) : Set.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        return email;
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
}
