package com.doranco.site.service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
import com.doranco.site.dto.ArticleRequest;
import com.doranco.site.exception.APIException;
import com.doranco.site.exception.ResourceNotFoundException;
import com.doranco.site.model.Categorie;
import com.doranco.site.model.Panier;
import com.doranco.site.model.Produit;
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
	public ArticleDTO ajouterArticle(Produit article, String categorieNom, MultipartFile image) throws IOException {
		
		System.out.println("Nom de la catégorie reçu : " + categorieNom);
	    // Récupérer la catégorie depuis la base de données
		Categorie categorie = categorieRepo.findByCategoryName(categorieNom);

	    // Vérifier si le produit existe déjà dans cette catégorie
	    boolean produitNonPrésent = true;
	    List<Produit> produits = categorie.getArticles();
	    for (Produit p : produits) {
	        if (p.getProductName().equals(article.getProductName()) && p.getDescription().equals(article.getDescription())) {
	            produitNonPrésent = false;
	            break;
	        }
	    }

	    if (produitNonPrésent) {
	        // Si la catégorie est "vetement", on attend des informations supplémentaires (sous-catégorie et taille)
	        if (categorie.getCategoryName().equalsIgnoreCase("vetement")) {
	            // Vérification de la sous-catégorie
	            if (article.getSousCategorie() == null && article.getTaille() == null) {
	                throw new APIException("Sous-catégorie et taille sont requises pour la catégorie vetement");
	            }
	        }
	        
	     // Si la catégorie est "volant", on attend des informations sur le type de volant
	        if (categorie.getCategoryName().equalsIgnoreCase("volant")) {
	            if (article.getSousCategorie() == null) {
	                throw new APIException("Le type de volant est requis pour la catégorie 'volant' (plastique, plume, hybride).");
	            }
	        }
	        
	     // Si la catégorie est "raquette", on attend des informations sur le type de volant
	        if (categorie.getCategoryName().equalsIgnoreCase("raquette")) {
	            if (article.getMarque() == null) {
	                throw new APIException("La marque de la raquette est requis");
	            }
	        }
	        
	     // Si la catégorie est "chaussure", on attend des informations sur le type de volant
	        if (categorie.getCategoryName().equalsIgnoreCase("chaussure")) {
	            if (article.getGenre() == null && article.getPointure() == null) {
	                throw new APIException("La pointure et la marque de la chaussure est requis");
	            }
	        }
	        
	        if (categorie.getCategoryName().equalsIgnoreCase("bagagerie")) {
	        	if (article.getTaille() == null) {
	                throw new APIException("La taille du sac est requis");
	            }
	        }

	        if (image != null && !image.isEmpty()) {
	            article.setImage(image.getBytes());
	        } else {
	            // Définir une image par défaut si aucune image n'est fournie (ou la laisser vide)
	            article.setImage(null);  // Ou article.setImage(defaultImageBytes) si vous avez une image par défaut
	        }
	        
	        // Associer l'article à sa catégorie
	        article.setCategorie(categorie);
	      
	        // Calculer le prix spécial en fonction de la remise
	        double prixSpecial = article.getPrix() - ((article.getRemise() * 0.01) * article.getPrix());
	        article.setPrixSpecial(prixSpecial);

	        // Enregistrer l'article dans la base de données
	        Produit produitEnregistre = articleRepo.save(article);

	        // Retourner l'article enregistré sous forme de DTO
	        return modelMapper.map(produitEnregistre, ArticleDTO.class);
	    } else {
	        throw new APIException("Produit déjà existant !!!");
	    }
	}
	
	
	public List<ArticleDTO> obtenirArticlesEnPromotion() {
        List<Produit> produitsEnPromotion = articleRepo.findAll()
                .stream()
                .filter(produit -> produit.getRemise() > 0)
                .collect(Collectors.toList());

        return produitsEnPromotion.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

	@Override
	public List<ArticleDTO> obtenirTousLesArticlesSansPagination() {
        // Utilisation du repository pour récupérer tous les articles
        List<Produit> articles = articleRepo.findAll();

        // Conversion des entités Article en ArticleDTO (vous devrez peut-être mapper les objets)
        return articles.stream().map(article -> convertToDTO(article))
            .collect(Collectors.toList());
    }

	private ArticleDTO convertToDTO(Produit article) {
	    String imageBase64 = article.getImage() != null ? 
	            Base64.getEncoder().encodeToString(article.getImage()) : null;
	        String imageUrl = imageBase64 != null ? "data:image/jpeg;base64," + imageBase64 : null;

        return new ArticleDTO(article.getIdProduit(), article.getProductName(), article.getDescription(), imageUrl, article.getQuantite(), article.getPrix(), article.getRemise(), article.getPrixSpecial(), article.getMarque());
    }
	
	@Override
	public ArticleReponse rechercherParCategorie(Long catégorieId, Integer numéroPage, Integer taillePage, String trierPar,
			String ordreTri) {

		Categorie catégorie = categorieRepo.findById(catégorieId)
				.orElseThrow(() -> new ResourceNotFoundException("Categorie", "categorieId", catégorieId));

		Sort triParEtOrdre = ordreTri.equalsIgnoreCase("asc") ? Sort.by(trierPar).ascending()
				: Sort.by(trierPar).descending();

		Pageable détailsPage = PageRequest.of(numéroPage, taillePage, triParEtOrdre);

		Page<Produit> pageProduits = articleRepo.findAll(détailsPage);

		List<Produit> produits = pageProduits.getContent();

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
	public ArticleReponse rechercherArticleParMotCle(String motClé, Integer numéroPage, Integer taillePage, String trierPar, String ordreTri) {
		Sort triParEtOrdre = ordreTri.equalsIgnoreCase("asc") ? Sort.by(trierPar).ascending()
				: Sort.by(trierPar).descending();

		Pageable détailsPage = PageRequest.of(numéroPage, taillePage, triParEtOrdre);

		Page<Produit> pageProduits = articleRepo.findByProductNameLike(motClé, détailsPage);

		List<Produit> articles = pageProduits.getContent();
		
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
	public ArticleDTO mettreAJourArticle(Long articleId, Produit article) {
		Produit produitDB = articleRepo.findById(articleId)
				.orElseThrow(() -> new ResourceNotFoundException("Produit", "produitId", articleId));

		if (produitDB == null) {
			throw new APIException("Produit non trouvé avec produitId: " + articleId);
		}

		article.setImage(produitDB.getImage());
		article.setCategorie(produitDB.getCategorie());

		double prixSpecial = article.getPrix() - ((article.getRemise() * 0.01) * article.getPrix());
		article.setPrixSpecial(prixSpecial);

		Produit produitEnregistré = articleRepo.save(article);

		List<Panier> paniers = panierRepo.findCartsByProductId(articleId);

		paniers.forEach(panier -> servicePanier.mettreAJourArticleDansPaniers(panier.getPanierId(), articleId));

		return modelMapper.map(produitEnregistré, ArticleDTO.class);
	}

	@Override
	public ArticleDTO mettreAJourImageArticle(Long articleId, MultipartFile image) throws IOException {
	    // Récupérer le produit à partir de l'ID
	    Produit produitDB = articleRepo.findById(articleId)
	            .orElseThrow(() -> new ResourceNotFoundException("Produit", "produitId", articleId));

	    // Vérifier si le produit existe
	    if (produitDB == null) {
	        throw new APIException("Produit non trouvé avec produitId: " + articleId);
	    }

	    // Vérifier que l'image n'est pas vide
	    if (image != null && !image.isEmpty()) {
	        // Convertir l'image en tableau de bytes
	        byte[] imageBytes = image.getBytes();
	        
	        // Mettre à jour l'image dans le produit
	        produitDB.setImage(imageBytes);
	    } else {
	        throw new APIException("Le fichier d'image est vide ou n'a pas été fourni.");
	    }

	    // Enregistrer le produit mis à jour dans la base de données
	    Produit produitMisAJour = articleRepo.save(produitDB);

	    // Retourner le produit mis à jour sous forme de DTO
	    return modelMapper.map(produitMisAJour, ArticleDTO.class);
	}


	@Override
	public String supprimerArticle(Long articleId) {

		Produit produit = articleRepo.findById(articleId)
				.orElseThrow(() -> new ResourceNotFoundException("Produit", "produitId", articleId));

		List<Panier> paniers = panierRepo.findCartsByProductId(articleId);

		paniers.forEach(panier -> servicePanier.supprimerArticleDuPanier(panier.getPanierId(), articleId));

		articleRepo.delete(produit);

		return "Produit avec produitId: " + articleId + " supprimé avec succès !!!";
	}
	
	public Optional<Produit> getProduitByName(String productName) {
		try {
	        return articleRepo.findByProductNameIgnoreCase(productName);
	    } catch (NoSuchElementException e) {
	        throw new RuntimeException("Produit non trouvé : " + productName);
	    } catch (Exception e) {
	        throw new RuntimeException("Erreur lors de la récupération du produit : " + e.getMessage());
	    }
    }
}
