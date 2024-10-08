package com.doranco.site.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.doranco.site.model.Adresse;

@Repository
public interface AdresseRepository extends JpaRepository<Adresse, Long> {

	Adresse findByCountryAndCityAndPincodeAndStreetAndBuildingName(String country, String city,
			String pincode, String street, String buildingName);
	
}
