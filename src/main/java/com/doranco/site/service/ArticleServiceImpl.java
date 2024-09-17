package com.doranco.site.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.doranco.site.dto.ArticleDTO;
import com.doranco.site.dto.ArticleReponse;
import com.doranco.site.exception.APIException;
import com.doranco.site.exception.ResourceNotFoundException;
import com.doranco.site.model.Article;
import com.doranco.site.model.Panier;
import com.doranco.site.model.Categorie;
import com.doranco.site.repository.ArticleRepository;
import com.doranco.site.repository.CategorieRepository;
import com.doranco.site.repository.PanierRepository;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleRepository articleRepo;

	@Autowired
	private CategorieRepository categorieRepo;

	@Autowired
	private PanierRepository panierRepo;

	@Autowired
	private PanierService servicePanier;

	@Autowired
	private FichierService serviceFichier;

	@Autowired
	private ModelMapper modelMapper;

	@Value("${project.image}")
	private String chemin;

	@Override
	public ArticleDTO ajouterArticle(String categorieNom, Article article) {

	    // Récupérer la catégorie depuis la base de données
		Categorie categorie = categorieRepo.findByCategoryName(categorieNom);

	    // Vérifier si le produit existe déjà dans cette catégorie
	    boolean produitNonPrésent = true;
	    List<Article> produits = categorie.getArticles();
	    for (Article p : produits) {
	        if (p.getProductName().equals(article.getProductName()) && p.getDescription().equals(article.getDescription())) {
	            produitNonPrésent = false;
	            break;
	        }
	    }

	    if (produitNonPrésent) {
	        // Si la catégorie est "tee-shirt", on attend des informations supplémentaires (sous-catégorie et taille)
	        if (categorie.getCategoryName().equalsIgnoreCase("tee-shirt")) {
	            // Vérification de la sous-catégorie
	            if (article.getSousCategorie() == null || article.getTaille() == null) {
	                throw new APIException("Sous-catégorie et taille sont requises pour la catégorie tee-shirt");
	            }
	        }

	        // Définir une image par défaut si aucune image n'est fournie
	        article.setImage("defaut.png");

	        // Associer l'article à sa catégorie
	        article.setCategorie(categorie);

	        // Calculer le prix spécial en fonction de la remise
	        double prixSpecial = article.getPrix() - ((article.getRemise() * 0.01) * article.getPrix());
	        article.setPrixSpecial(prixSpecial);

	        // Enregistrer l'article dans la base de données
	        Article produitEnregistre = articleRepo.save(article);

	        // Retourner l'article enregistré sous forme de DTO
	        return modelMapper.map(produitEnregistre, ArticleDTO.class);
	    } else {
	        throw new APIException("Produit déjà existant !!!");
	    }
	}

	@Override
	public ArticleReponse obtenirTousLesArticles(Integer numéroPage, Integer taillePage, String trierPar, String ordreTri) {

		Sort triParEtOrdre = ordreTri.equalsIgnoreCase("asc") ? Sort.by(trierPar).ascending()
				: Sort.by(trierPar).descending();

		Pageable détailsPage = PageRequest.of(numéroPage, taillePage, triParEtOrdre);

		Page<Article> pageProduits = articleRepo.findAll(détailsPage);

		List<Article> articles = pageProduits.getContent();

		List<ArticleDTO> produitDTOs = articles.stream().map(produit -> modelMapper.map(produit, ArticleDTO.class))
				.collect(Collectors.toList());

		ArticleReponse réponseProduit = new ArticleReponse();

		réponseProduit.setContenu(produitDTOs);
		réponseProduit.setNumeroPage(pageProduits.getNumber());
		réponseProduit.setTaillePage(pageProduits.getSize());
		réponseProduit.setTotalElements(pageProduits.getTotalElements());
		réponseProduit.setTotalPages(pageProduits.getTotalPages());
		réponseProduit.setDernierePage(pageProduits.isLast());

		return réponseProduit;
	}

	@Override
	public ArticleReponse rechercherParCatégorie(Long catégorieId, Integer numéroPage, Integer taillePage, String trierPar,
			String ordreTri) {

		Categorie catégorie = categorieRepo.findById(catégorieId)
				.orElseThrow(() -> new ResourceNotFoundException("Categorie", "categorieId", catégorieId));

		Sort triParEtOrdre = ordreTri.equalsIgnoreCase("asc") ? Sort.by(trierPar).ascending()
				: Sort.by(trierPar).descending();

		Pageable détailsPage = PageRequest.of(numéroPage, taillePage, triParEtOrdre);

		Page<Article> pageProduits = articleRepo.findAll(détailsPage);

		List<Article> produits = pageProduits.getContent();

		if (produits.isEmpty()) {
			throw new APIException(catégorie.getCategoryName() + " n'a pas de produits !!!");
		}

		List<ArticleDTO> produitDTOs = produits.stream().map(p -> modelMapper.map(p, ArticleDTO.class))
				.collect(Collectors.toList());

		ArticleReponse réponseProduit = new ArticleReponse();

		réponseProduit.setContenu(produitDTOs);
		réponseProduit.setNumeroPage(pageProduits.getNumber());
		réponseProduit.setTaillePage(pageProduits.getSize());
		réponseProduit.setTotalElements(pageProduits.getTotalElements());
		réponseProduit.setTotalPages(pageProduits.getTotalPages());
		réponseProduit.setDernierePage(pageProduits.isLast());

		return réponseProduit;
	}

	@Override
	public ArticleReponse rechercherArticleParMotClé(String motClé, Integer numéroPage, Integer taillePage, String trierPar, String ordreTri) {
		Sort triParEtOrdre = ordreTri.equalsIgnoreCase("asc") ? Sort.by(trierPar).ascending()
				: Sort.by(trierPar).descending();

		Pageable détailsPage = PageRequest.of(numéroPage, taillePage, triParEtOrdre);

		Page<Article> pageProduits = articleRepo.findByProductNameLike(motClé, détailsPage);

		List<Article> articles = pageProduits.getContent();
		
		if (articles.isEmpty()) {
			throw new APIException("Aucun produit trouvé avec le mot-clé : " + motClé);
		}

		List<ArticleDTO> produitDTOs = articles.stream().map(p -> modelMapper.map(p, ArticleDTO.class))
				.collect(Collectors.toList());

		ArticleReponse réponseProduit = new ArticleReponse();

		réponseProduit.setContenu(produitDTOs);
		réponseProduit.setNumeroPage(pageProduits.getNumber());
		réponseProduit.setTaillePage(pageProduits.getSize());
		réponseProduit.setTotalElements(pageProduits.getTotalElements());
		réponseProduit.setTotalPages(pageProduits.getTotalPages());
		réponseProduit.setDernierePage(pageProduits.isLast());

		return réponseProduit;
	}

	@Override
	public ArticleDTO mettreÀJourArticle(Long articleId, Article article) {
		Article produitDB = articleRepo.findById(articleId)
				.orElseThrow(() -> new ResourceNotFoundException("Produit", "produitId", articleId));

		if (produitDB == null) {
			throw new APIException("Produit non trouvé avec produitId: " + articleId);
		}

		article.setImage(produitDB.getImage());
		article.setIdArticle(articleId);
		article.setCategorie(produitDB.getCategorie());

		double prixSpecial = article.getPrix() - ((article.getRemise() * 0.01) * article.getPrix());
		article.setPrixSpecial(prixSpecial);

		Article produitEnregistré = articleRepo.save(article);

		List<Panier> paniers = panierRepo.findCartsByProductId(articleId);

		paniers.forEach(panier -> servicePanier.mettreÀJourArticleDansPaniers(panier.getPanierId(), articleId));

		return modelMapper.map(produitEnregistré, ArticleDTO.class);
	}

	@Override
	public ArticleDTO mettreÀJourImageArticle(Long articleId, MultipartFile image) throws IOException {
		Article produitDB = articleRepo.findById(articleId)
				.orElseThrow(() -> new ResourceNotFoundException("Produit", "produitId", articleId));

		if (produitDB == null) {
			throw new APIException("Produit non trouvé avec produitId: " + articleId);
		}
		
		String nomFichier = serviceFichier.téléverserImage(chemin, image);
		
		produitDB.setImage(nomFichier);
		
		Article produitMisAJour = articleRepo.save(produitDB);
		
		return modelMapper.map(produitMisAJour, ArticleDTO.class);
	}

	@Override
	public String supprimerArticle(Long articleId) {

		Article produit = articleRepo.findById(articleId)
				.orElseThrow(() -> new ResourceNotFoundException("Produit", "produitId", articleId));

		List<Panier> paniers = panierRepo.findCartsByProductId(articleId);

		paniers.forEach(panier -> servicePanier.supprimerArticleDuPanier(panier.getPanierId(), articleId));

		articleRepo.delete(produit);

		return "Produit avec produitId: " + articleId + " supprimé avec succès !!!";
	}

}
