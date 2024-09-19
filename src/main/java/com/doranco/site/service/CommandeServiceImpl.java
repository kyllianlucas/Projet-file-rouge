package com.doranco.site.service;

import java.time.LocalDate;
import java.util.ArrayList;
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

import com.doranco.site.dto.ArticleCommandeDTO;
import com.doranco.site.dto.CommandeDTO;
import com.doranco.site.dto.CommandeReponse;
import com.doranco.site.exception.APIException;
import com.doranco.site.exception.ResourceNotFoundException;
import com.doranco.site.model.*;
import com.doranco.site.repository.ArticleCommandeRepository;
import com.doranco.site.repository.ArticlePanierRepository;
import com.doranco.site.repository.CommandeRepository;
import com.doranco.site.repository.PaiementRepository;
import com.doranco.site.repository.PanierRepository;
import com.doranco.site.repository.UtilisateurRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Transactional
@Service
public class CommandeServiceImpl implements CommandeService {

	@Autowired
	public UtilisateurRepository utilisateurRepo;

	@Autowired
	public PanierRepository panierRepo;

	@Autowired
	public CommandeRepository commandeRepo;

	@Autowired
	private PaiementRepository paiementRepo;

	@Autowired
	public ArticleCommandeRepository articleCommandeRepo;

	@Autowired
	public ArticlePanierRepository articlePanierRepo;

	@Autowired
	public UserService serviceUtilisateur;

	@Autowired
	public PanierService servicePanier;
	
	@Autowired
	public StripeService stripeService;

	@Autowired
	public ModelMapper modelMapper;
	
	@Value("${stripe.api.key}")
    private String stripeApiKey;
	
	
	@PostConstruct
	public void init() {
		Stripe.apiKey= stripeApiKey;
	}

	@Override
	public CommandeDTO passerCommande(String emailId, Long panierId, String méthodePaiement,  String stripeToken) {

		Panier panier = panierRepo.findCartByEmailAndCartId(emailId, panierId);

		if (panier == null) {
			throw new ResourceNotFoundException("Panier", "panierId", panierId);
		}

		Commande commande = new Commande();

		commande.setEmail(emailId);
		commande.setDateCommande(LocalDate.now());

		commande.setMontantTotal(panier.getPrixTotal());
		commande.setStatutCommande("Commande acceptée !");

		Paiement paiement = new Paiement();
		paiement.setCommande(commande);
		paiement.setModePaiement(méthodePaiement);		
		paiement = paiementRepo.save(paiement);
		commande.setPaiement(paiement);
		
		// Convertir le montant de Double à long (en centimes)
	    long montantEnCentimes = (long) Math.round(panier.getPrixTotal() * 100);
		
		try {
	        Charge charge = stripeService.chargeCard(stripeToken, montantEnCentimes, "eur"); // Assurez-vous que le montant est en cents
	        paiement.setModePaiement("Stripe");
	        paiementRepo.save(paiement);
	    } catch (StripeException e) {
	        throw new APIException("Erreur de paiement avec Stripe : " + e.getMessage());
	    }

		Commande commandeSauvegardée = commandeRepo.save(commande);

		List<ArticlePanier> articlesPanier = panier.getItemsPanier();

		if (articlesPanier.size() == 0) {
			throw new APIException("Le panier est vide");
		}

		List<ArticleCommande> articlesCommande = new ArrayList<>();

		for (ArticlePanier articlePanier : articlesPanier) {
			ArticleCommande articleCommande = new ArticleCommande();

			articleCommande.setProduit(articlePanier.getProduit());
			articleCommande.setQuantite(articlePanier.getQuantite());
			articleCommande.setRemise(articlePanier.getRemise());
			articleCommande.setPrixProduitCommande(articlePanier.getPrixProduit());
			articleCommande.setCommande(commandeSauvegardée);

			articlesCommande.add(articleCommande);
		}

		articlesCommande = articleCommandeRepo.saveAll(articlesCommande);

		panier.getItemsPanier().forEach(item -> {
			int quantité = item.getQuantite();

			Produit produit = item.getProduit();

			servicePanier.supprimerArticleDuPanier(panierId, item.getProduit().getIdProduit());

			produit.setQuantite(produit.getQuantite() - quantité);
		});

		CommandeDTO commandeDTO = modelMapper.map(commandeSauvegardée, CommandeDTO.class);
		
		articlesCommande.forEach(item -> commandeDTO.getArticlesCommande().add(modelMapper.map(item, ArticleCommandeDTO.class)));

		return commandeDTO;
	}

	@Override
	public List<CommandeDTO> obtenirCommandesParUtilisateur(String emailId) {
		List<Commande> commandes = commandeRepo.findAllByEmail(emailId);

		List<CommandeDTO> commandesDTO = commandes.stream().map(commande -> modelMapper.map(commande, CommandeDTO.class))
				.collect(Collectors.toList());

		if (commandesDTO.size() == 0) {
			throw new APIException("Aucune commande n'a encore été passée par l'utilisateur avec l'email : " + emailId);
		}

		return commandesDTO;
	}

	@Override
	public CommandeDTO obtenirCommande(String emailId, Long commandeId) {

		Commande commande = commandeRepo.findCommandeByEmailAndCommandeId(emailId, commandeId);

		if (commande == null) {
			throw new ResourceNotFoundException("Commande", "commandeId", commandeId);
		}

		return modelMapper.map(commande, CommandeDTO.class);
	}

	@Override
	public CommandeReponse obtenirToutesLesCommandes(Integer numéroPage, Integer taillePage, String trierPar, String ordreTri) {

		Sort trierParEtOrdre = ordreTri.equalsIgnoreCase("asc") ? Sort.by(trierPar).ascending()
				: Sort.by(trierPar).descending();

		Pageable détailsPage = PageRequest.of(numéroPage, taillePage, trierParEtOrdre);

		Page<Commande> pageCommandes = commandeRepo.findAll(détailsPage);

		List<Commande> commandes = pageCommandes.getContent();

		List<CommandeDTO> commandesDTOs = commandes.stream().map(commande -> modelMapper.map(commande, CommandeDTO.class))
				.collect(Collectors.toList());
		
		if (commandesDTOs.size() == 0) {
			throw new APIException("Aucune commande n'a encore été passée par les utilisateurs");
		}

		CommandeReponse réponseCommande = new CommandeReponse();
		
		réponseCommande.setContenu(commandesDTOs);
		réponseCommande.setNumeroPage(pageCommandes.getNumber());
		réponseCommande.setTaillePage(pageCommandes.getSize());
		réponseCommande.setTotalElements(pageCommandes.getTotalElements());
		réponseCommande.setTotalPages(pageCommandes.getTotalPages());
		réponseCommande.setDernierePage(pageCommandes.isLast());
		
		return réponseCommande;
	}

	@Override
	public CommandeDTO mettreAJourCommande(String emailId, Long commandeId, String statutCommande) {

		Commande commande = commandeRepo.findCommandeByEmailAndCommandeId(emailId, commandeId);

		if (commande == null) {
			throw new ResourceNotFoundException("Commande", "commandeId", commandeId);
		}

		commande.setStatutCommande(statutCommande);

		return modelMapper.map(commande, CommandeDTO.class);
	}

}
