import com.doranco.site.model.Produit;
import com.doranco.site.repository.ProduitRepository;
import com.doranco.site.service.ProduitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProduitServiceTest {

    @Mock
    private ProduitRepository produitRepository;

    @InjectMocks
    private ProduitService produitService;

    private Produit produit;

    @BeforeEach
    void setUp() {
        produit = new Produit();
        produit.setId(1L);
        produit.setNom("Produit test");
        produit.setDescription("Description du produit test");
        produit.setPrix(new BigDecimal("25.50"));
        produit.setQuantiteStock(10);
        produit.setMarque("Marque test");
        produit.setDisponible(true);
    }

    @Test
    void testFindById() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        Produit produitFound = produitService.findById(1L);
        assertNotNull(produitFound);
        assertEquals("Produit test", produitFound.getNom());
    }

    @Test
    void testAddProduit() {
        when(produitRepository.save(produit)).thenReturn(produit);
        Produit produitAdded = produitService.addProduit(produit);
        assertNotNull(produitAdded);
        assertEquals("Marque test", produitAdded.getMarque());
    }

    @Test
    void testUpdateQuantite() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        Produit produitMisAJour = produitService.updateQuantite(1L, 5);
        assertEquals(5, produitMisAJour.getQuantiteStock());
        assertEquals(true, produitMisAJour.isDisponible());
    }

    @Test
    void testUpdateQuantiteZero() {
        when(produitRepository.findById(1L)).thenReturn(Optional.of(produit));
        Produit produitMisAJour = produitService.updateQuantite(1L, 0);
        assertEquals(0, produitMisAJour.getQuantiteStock());
        assertEquals(false, produitMisAJour.isDisponible());
    }

    @Test
    void testDeleteProduit() {
        produitService.deleteProduit(1L);
        verify(produitRepository, times(1)).deleteById(1L);
    }
}
