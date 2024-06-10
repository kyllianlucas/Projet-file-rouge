import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doranco.site.exception.UserNotFoundException;
import com.doranco.site.model.Adresse;
import com.doranco.site.model.Commande;
import com.doranco.site.model.User;
import com.doranco.site.repository.AdresseRepository;
import com.doranco.site.repository.UserRepository;

@Service
public class AdresseService {

    @Autowired
    private AdresseRepository adresseRepository;
    
    @Autowired
    private UserRepository userRepository;

    public List<Adresse> getAllAdresses() {
        return adresseRepository.findAll();
    }

    public Optional<Adresse> getAdresseById(Long id) {
        return adresseRepository.findById(id);
    }

    public Adresse saveAdresse(Adresse adresse, long id) {
        return adresseRepository.save(adresse);
    }

    public void deleteAdresse(Long adresseId) {
        adresseRepository.deleteById(adresseId);
    }

    public Adresse addAdresse(Long userId, Adresse adresse) throws UserNotFoundException {
        // Rechercher l'utilisateur par son ID
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Utilisateur non trouvé avec l'ID : " + userId);
        }
        
        // Lier l'adresse à l'utilisateur
        User user = userOptional.get();
        adresse.setUser(user);

        // Enregistrer l'adresse
        return adresseRepository.save(adresse);
    }
    
    public Adresse saveAdresseWithCommande(Adresse adresse, Commande commande) {
        User user = commande.getUser();
        if (!userHasAddress(user)) {
            adresse.setCommande(commande); // Associer l'adresse à la commande
            return adresseRepository.save(adresse); // Enregistrer l'adresse avec la commande
        } else {
            throw new RuntimeException("L'utilisateur a déjà une adresse enregistrée.");
        }
    }

	private boolean userHasAddress(User user) {
		List<Adresse> adresses = adresseRepository.findByUser(user);
        return !adresses.isEmpty();
	}

}
