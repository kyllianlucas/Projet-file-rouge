package com.doranco.site.repository;

import java.util.List;
import java.util.Optional;

import com.doranco.site.model.Adresse;
import com.doranco.site.model.User;

public interface AdresseRepository {

	void deleteById(Long adresseId);

	Optional<Adresse> findById(Long id);

	Adresse save(Adresse adresse);

	List<Adresse> findAll();

	boolean existsById(Long id);

	List<Adresse> findByUser(User user);

}
