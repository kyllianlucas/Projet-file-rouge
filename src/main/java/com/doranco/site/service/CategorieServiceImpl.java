package com.doranco.site.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.doranco.site.dto.CategorieDTO;
import com.doranco.site.dto.CategorieReponse;
import com.doranco.site.exception.APIException;
import com.doranco.site.exception.ResourceNotFoundException;
import com.doranco.site.model.Categorie;
import com.doranco.site.model.Produit;
import com.doranco.site.repository.CategorieRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class CategorieServiceImpl implements CategorieService {

	@Autowired
	private CategorieRepository categorieRepo;
	
	@Autowired
	private ArticleService produitService;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategorieDTO creerCategorie(Categorie categorie) {
		Categorie categorieEnregistree = categorieRepo.findByCategoryName(categorie.getCategoryName());

		if (categorieEnregistree != null) {
			throw new APIException("Une catégorie avec le nom '" + categorie.getCategoryName() + "' existe déjà !!!");
		}

		categorieEnregistree = categorieRepo.save(categorie);

		return modelMapper.map(categorieEnregistree, CategorieDTO.class);
	}

	@Override
	public CategorieReponse obtenirCategories(Integer numeroPage, Integer taillePage, String trierPar, String ordreTri) {
		Sort trierParEtOrdre = ordreTri.equalsIgnoreCase("asc") ? Sort.by(trierPar).ascending()
				: Sort.by(trierPar).descending();

		Pageable detailsPage = PageRequest.of(numeroPage, taillePage, trierParEtOrdre);
		
		Page<Categorie> pageCategories = categorieRepo.findAll(detailsPage);

		List<Categorie> categories = pageCategories.getContent();

		if (categories.size() == 0) {
			throw new APIException("Aucune catégorie n'a été créée jusqu'à présent.");
		}

		List<CategorieDTO> categorieDTOs = categories.stream()
				.map(categorie -> modelMapper.map(categorie, CategorieDTO.class)).collect(Collectors.toList());

		CategorieReponse categorieRéponse = new CategorieReponse();
		
		categorieRéponse.setContenu(categorieDTOs);
		categorieRéponse.setNumeroPage(pageCategories.getNumber());
		categorieRéponse.setTaillePage(pageCategories.getSize());
		categorieRéponse.setTotalElements(pageCategories.getTotalElements());
		categorieRéponse.setTotalPages(pageCategories.getTotalPages());
		categorieRéponse.setDernierePage(pageCategories.isLast());
		
		return categorieRéponse;
	}

	@Override
	public CategorieDTO mettreAJourCategorie(Categorie categorie, Long categorieId) {
		Categorie categorieEnregistree = categorieRepo.findById(categorieId)
				.orElseThrow(() -> new ResourceNotFoundException("Catégorie", "categorieId", categorieId));

		categorie.setIdCategorie(categorieId);

		categorieEnregistree = categorieRepo.save(categorie);

		return modelMapper.map(categorieEnregistree, CategorieDTO.class);
	}

	@Override
	public String supprimerCategorie(Long categorieId) {
		Categorie categorie = categorieRepo.findById(categorieId)
				.orElseThrow(() -> new ResourceNotFoundException("Catégorie", "categorieId", categorieId));
		
		List<Produit> produits = categorie.getArticles();

		produits.forEach(produit -> {
			produitService.supprimerArticle(produit.getIdProduit());  
		});
		
		categorieRepo.delete(categorie);

		return "Catégorie avec categorieId: " + categorieId + " supprimée avec succès !!!";
	}

}
