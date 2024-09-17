package com.doranco.site.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.doranco.site.dto.ArticleDTO;
import com.doranco.site.dto.ArticleReponse;
import com.doranco.site.exception.APIException;
import com.doranco.site.model.Article;
import com.doranco.site.model.Categorie;
import com.doranco.site.repository.ArticleRepository;
import com.doranco.site.repository.CategorieRepository;
import com.doranco.site.repository.PanierRepository;

	public class ArticleServiceTest {
	

	    @Mock
	    private ArticleRepository articleRepo;

	    @Mock
	    private CategorieRepository categorieRepo;

	    @Mock
	    private PanierRepository panierRepo;

	    @Mock
	    private PanierService panierService;

	    @Mock
	    private FichierService fichierService;

	    @Mock
	    private ModelMapper modelMapper;

	    @InjectMocks
	    private ArticleServiceImpl articleService;

	    @Value("${projet.image}")
	    private String chemin;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    void testAjouterArticle_Succès() {
	        Long categorieId = 1L;
	        Categorie categorie = new Categorie();
	        categorie.setIdCategorie(categorieId);
	        
	        // Initialiser la liste des articles
	        categorie.setArticles(new ArrayList<>());

	        Article article = new Article();
	        article.setProductName("Produit Test");
	        article.setDescription("Description Test");
	        article.setPrix(100.0); // Assurez-vous que le prix et la remise ne sont pas nulls
	        article.setRemise(10.0);

	        // Simulation des méthodes appelées
	        when(categorieRepo.findById(categorieId)).thenReturn(Optional.of(categorie));
	        when(articleRepo.save(any(Article.class))).thenReturn(article);
	        
	        // Assurez-vous que ModelMapper fonctionne correctement
	        ArticleDTO articleDTO = new ArticleDTO();
	        when(modelMapper.map(any(Article.class), eq(ArticleDTO.class))).thenReturn(articleDTO);

	        // Appel de la méthode à tester
	        ArticleDTO result = articleService.ajouterArticle(categorieId, article);

	        // Vérification des résultats
	        assertNotNull(result);
	        verify(articleRepo, times(1)).save(any(Article.class));
	    }

	    @Test
	    void testAjouterArticle_ProduitDéjàExistant() {
	        Long categorieId = 1L;
	        Categorie categorie = new Categorie();
	        categorie.setIdCategorie(categorieId);

	        Article existingArticle = new Article();
	        existingArticle.setProductName("Produit Test");
	        existingArticle.setDescription("Description Test");

	        categorie.setArticles(List.of(existingArticle));

	        Article newArticle = new Article();
	        newArticle.setProductName("Produit Test");
	        newArticle.setDescription("Description Test");

	        when(categorieRepo.findById(categorieId)).thenReturn(Optional.of(categorie));

	        APIException exception = assertThrows(APIException.class, () -> {
	            articleService.ajouterArticle(categorieId, newArticle);
	        });

	        assertEquals("Produit déjà existant !!!", exception.getMessage());
	    }

	    @Test
	    void testObtenirTousLesArticles_Succès() {
	        int numeroPage = 0;
	        int taillePage = 5;
	        String trierPar = "nomProduit";
	        String ordreTri = "asc";

	        List<Article> articles = Arrays.asList(new Article(), new Article());
	        Page<Article> pageProduits = new PageImpl<>(articles);

	        when(articleRepo.findAll(any(Pageable.class))).thenReturn(pageProduits);

	        ArticleReponse articleReponse = articleService.obtenirTousLesArticles(numeroPage, taillePage, trierPar, ordreTri);

	        assertNotNull(articleReponse);
	        assertEquals(2, articleReponse.getContenu().size());
	        verify(articleRepo, times(1)).findAll(any(Pageable.class));
	    }

	    @Test
	    void testRechercherArticleParMotClé_Succès() {
	        String motClé = "Test";
	        int numeroPage = 0;
	        int taillePage = 5;
	        String trierPar = "nomProduit";
	        String ordreTri = "asc";

	        List<Article> articles = Arrays.asList(new Article(), new Article());
	        Page<Article> pageProduits = new PageImpl<>(articles);

	        when(articleRepo.findByProductNameLike(eq(motClé), any(Pageable.class))).thenReturn(pageProduits);

	        ArticleReponse articleReponse = articleService.rechercherArticleParMotClé(motClé, numeroPage, taillePage, trierPar, ordreTri);

	        assertNotNull(articleReponse);
	        assertEquals(2, articleReponse.getContenu().size());
	        verify(articleRepo, times(1)).findByProductNameLike(eq(motClé), any(Pageable.class));
	    }

	    @Test
	    void testMettreÀJourArticle_Succès() {
	        Long articleId = 1L;
	        Article existingArticle = new Article();
	        existingArticle.setIdArticle(articleId);

	        Article updatedArticle = new Article();
	        updatedArticle.setProductName("Updated");

	        when(articleRepo.findById(articleId)).thenReturn(Optional.of(existingArticle));
	        when(articleRepo.save(any(Article.class))).thenReturn(updatedArticle);
	        when(modelMapper.map(any(Article.class), eq(ArticleDTO.class))).thenReturn(new ArticleDTO());

	        ArticleDTO result = articleService.mettreÀJourArticle(articleId, updatedArticle);

	        assertNotNull(result);
	        verify(articleRepo, times(1)).save(any(Article.class));
	    }

	    @Test
	    void testMettreÀJourImageArticle_Succès() throws IOException {
	        Long articleId = 1L;

	        // Mock d'un article existant
	        Article existingArticle = new Article();
	        existingArticle.setIdArticle(articleId);

	        // Mock de MultipartFile
	        MultipartFile mockImage = mock(MultipartFile.class);
	        when(mockImage.getOriginalFilename()).thenReturn("image.jpg");

	        // Initialisation manuelle du chemin avec ReflectionTestUtils
	        ReflectionTestUtils.setField(articleService, "chemin", "/chemin/mocké");

	        // Simulation des appels aux méthodes
	        when(articleRepo.findById(articleId)).thenReturn(Optional.of(existingArticle));
	        when(fichierService.téléverserImage(eq("/chemin/mocké"), eq(mockImage))).thenReturn("image.jpg");
	        when(articleRepo.save(any(Article.class))).thenReturn(existingArticle);
	        when(modelMapper.map(any(Article.class), eq(ArticleDTO.class))).thenReturn(new ArticleDTO());

	        // Appel de la méthode à tester
	        ArticleDTO result = articleService.mettreÀJourImageArticle(articleId, mockImage);

	        // Vérifications
	        assertNotNull(result);
	        verify(fichierService, times(1)).téléverserImage(eq("/chemin/mocké"), eq(mockImage));
	        verify(articleRepo, times(1)).save(any(Article.class));
	    }



	    @Test
	    void testSupprimerArticle_Succès() {
	        Long articleId = 1L;
	        Article article = new Article();
	        article.setIdArticle(articleId);

	        when(articleRepo.findById(articleId)).thenReturn(Optional.of(article));

	        String result = articleService.supprimerArticle(articleId);

	        assertEquals("Produit avec produitId: " + articleId + " supprimé avec succès !!!", result);
	        verify(articleRepo, times(1)).delete(article);
	    }
	}
