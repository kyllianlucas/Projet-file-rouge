package com.doranco.site.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doranco.site.dto.ArticleDTO;
import com.doranco.site.dto.PanierDTO;
import com.doranco.site.exception.APIException;
import com.doranco.site.exception.ResourceNotFoundException;
import com.doranco.site.model.Article;
import com.doranco.site.model.ArticlePanier;
import com.doranco.site.model.Panier;
import com.doranco.site.repository.ArticlePanierRepository;
import com.doranco.site.repository.ArticleRepository;
import com.doranco.site.repository.PanierRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class PanierServiceImpl implements PanierService {

	@Autowired
	private PanierRepository panierRepo;

	@Autowired
	private ArticleRepository articleRepo;

	@Autowired
	private ArticlePanierRepository panierItemRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public PanierDTO ajouterArticleAuPanier(Long panierId, Long produitId, Integer quantité) {

		Panier panier = panierRepo.findById(panierId)
				.orElseThrow(() -> new ResourceNotFoundException("Panier", "panierId", panierId));

		Article article = articleRepo.findById(produitId)
				.orElseThrow(() -> new ResourceNotFoundException("Produit", "produitId", produitId));

		ArticlePanier panierItem = panierItemRepo.findCartItemByProductIdAndCartId(panierId, produitId);

		if (panierItem != null) {
			throw new APIException("Le produit " + article.getProductName() + " existe déjà dans le panier");
		}

		if (article.getQuantite() == 0) {
			throw new APIException(article.getProductName() + " n'est pas disponible");
		}

		if (article.getQuantite() < quantité) {
			throw new APIException("Veuillez commander une quantité du " + article.getProductName()
					+ " inférieure ou égale à " + article.getQuantite() + ".");
		}

		ArticlePanier nouveauPanierItem = new ArticlePanier();

		nouveauPanierItem.setProduit(article);
		nouveauPanierItem.setPanier(panier);
		nouveauPanierItem.setQuantite(quantité);
		nouveauPanierItem.setRemise(article.getRemise());
		nouveauPanierItem.setPrixProduit(article.getPrixSpecial());

		panierItemRepo.save(nouveauPanierItem);

		article.setQuantite(article.getQuantite() - quantité);

		panier.setPrixTotal(panier.getPrixTotal() + (article.getPrixSpecial() * quantité));

		PanierDTO panierDTO = modelMapper.map(panier, PanierDTO.class);

		List<ArticleDTO> produitDTOs = panier.getItemsPanier().stream()
				.map(p -> modelMapper.map(p.getProduit(), ArticleDTO.class)).collect(Collectors.toList());

		panierDTO.setArticles(produitDTOs);

		return panierDTO;
	}

	@Override
	public List<PanierDTO> obtenirTousLesPaniers() {
		List<Panier> paniers = panierRepo.findAll();

		if (paniers.size() == 0) {
			throw new APIException("Aucun panier n'existe");
		}

		List<PanierDTO> panierDTOs = paniers.stream().map(panier -> {
			PanierDTO panierDTO = modelMapper.map(panier, PanierDTO.class);

			List<ArticleDTO> produits = panier.getItemsPanier().stream()
					.map(p -> modelMapper.map(p.getProduit(), ArticleDTO.class)).collect(Collectors.toList());

			panierDTO.setArticles(produits);

			return panierDTO;
		}).collect(Collectors.toList());

		return panierDTOs;
	}

	@Override
	public PanierDTO obtenirPanier(String emailId, Long panierId) {
		Panier panier = panierRepo.findCartByEmailAndCartId(emailId, panierId);

		if (panier == null) {
			throw new ResourceNotFoundException("Panier", "panierId", panierId);
		}

		PanierDTO panierDTO = modelMapper.map(panier, PanierDTO.class);
		
		List<ArticleDTO> produits = panier.getItemsPanier().stream()
				.map(p -> modelMapper.map(p.getProduit(), ArticleDTO.class)).collect(Collectors.toList());

		panierDTO.setArticles(produits);

		return panierDTO;
	}

	@Override
	public void mettreÀJourArticleDansPaniers(Long panierId, Long produitId) {
		Panier panier = panierRepo.findById(panierId)
				.orElseThrow(() -> new ResourceNotFoundException("Panier", "panierId", panierId));

		Article produit = articleRepo.findById(produitId)
				.orElseThrow(() -> new ResourceNotFoundException("Produit", "produitId", produitId));

		ArticlePanier panierItem = panierItemRepo.findCartItemByProductIdAndCartId(panierId, produitId);

		if (panierItem == null) {
			throw new APIException("Le produit " + produit.getProductName() + " n'est pas disponible dans le panier!!!");
		}

		double prixPanier = panier.getPrixTotal() - (panierItem.getPrixProduit() * panierItem.getQuantite());

		panierItem.setPrixProduit(produit.getPrixSpecial());

		panier.setPrixTotal(prixPanier + (panierItem.getPrixProduit() * panierItem.getQuantite()));

		panierItem = panierItemRepo.save(panierItem);
	}

	@Override
	public PanierDTO mettreÀJourQuantitéArticleDansPanier(Long panierId, Long produitId, Integer quantité) {
		Panier panier = panierRepo.findById(panierId)
				.orElseThrow(() -> new ResourceNotFoundException("Panier", "panierId", panierId));

		Article produit = articleRepo.findById(produitId)
				.orElseThrow(() -> new ResourceNotFoundException("Produit", "produitId", produitId));

		if (produit.getQuantite() == 0) {
			throw new APIException(produit.getProductName() + " n'est pas disponible");
		}

		if (produit.getQuantite() < quantité) {
			throw new APIException("Veuillez commander une quantité du " + produit.getProductName()
					+ " inférieure ou égale à " + produit.getQuantite() + ".");
		}

		ArticlePanier panierItem = panierItemRepo.findCartItemByProductIdAndCartId(panierId, produitId);

		if (panierItem == null) {
			throw new APIException("Le produit " + produit.getProductName() + " n'est pas disponible dans le panier!!!");
		}

		double prixPanier = panier.getPrixTotal() - (panierItem.getPrixProduit() * panierItem.getQuantite());

		produit.setQuantite(produit.getQuantite() + panierItem.getQuantite() - quantité);

		panierItem.setPrixProduit(produit.getPrixSpecial());
		panierItem.setQuantite(quantité);
		panierItem.setRemise(produit.getRemise());

		panier.setPrixTotal(prixPanier + (panierItem.getPrixProduit() * quantité));

		panierItem = panierItemRepo.save(panierItem);

		PanierDTO panierDTO = modelMapper.map(panier, PanierDTO.class);

		List<ArticleDTO> produitDTOs = panier.getItemsPanier().stream()
				.map(p -> modelMapper.map(p.getProduit(), ArticleDTO.class)).collect(Collectors.toList());

		panierDTO.setArticles(produitDTOs);

		return panierDTO;
	}

	@Override
	public String supprimerArticleDuPanier(Long panierId, Long produitId) {
		Panier panier = panierRepo.findById(panierId)
				.orElseThrow(() -> new ResourceNotFoundException("Panier", "panierId", panierId));

		ArticlePanier panierItem = panierItemRepo.findCartItemByProductIdAndCartId(panierId, produitId);

		if (panierItem == null) {
			throw new ResourceNotFoundException("Produit", "produitId", produitId);
		}

		panier.setPrixTotal(panier.getPrixTotal() - (panierItem.getPrixProduit() * panierItem.getQuantite()));

		Article produit = panierItem.getProduit();
		produit.setQuantite(produit.getQuantite() + panierItem.getQuantite());

		panierItemRepo.deleteCartItemByProductIdAndCartId(panierId, produitId);

		return "Produit " + panierItem.getProduit().getProductName() + " supprimé du panier !!!";
	}
}
