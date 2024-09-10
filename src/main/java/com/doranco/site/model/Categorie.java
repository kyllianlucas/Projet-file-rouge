package com.doranco.site.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@Getter
@Setter
public class Categorie extends BaseCategorie
{
	 @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, orphanRemoval = true)
	    private Set<SousCategorie> sousCategories = new HashSet<>();
	 
	 @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL, orphanRemoval = true)
	    private Set<Article> articles = new HashSet<>();
}

