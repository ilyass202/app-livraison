package app.Backend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import app.Backend.model.Commande;
import app.Backend.model.Utilisateur;
import app.Backend.Repositry.CommandeRepositry;
import app.Backend.Repositry.UtilisateurRepositry;
import java.util.Date;
import java.util.UUID;
import java.util.Optional;
import app.Backend.model.Livreur;
import app.Backend.Repositry.LivreurRepositry;
import app.Backend.Dto.CommandeEnvoyeDto;
import app.Backend.Dto.commandeInfo;

@Service
public class CommandeService {
    @Autowired
    private CommandeRepositry commandeRepository;
    @Autowired
    private UtilisateurRepositry utilisateurRepository;
    @Autowired
    private LivreurRepositry livreurRepository;

    public Commande creerCommande(Long utilisateurId) {
        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
            .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        
        Commande commande = new Commande();
        commande.setUtilisateur(utilisateur);
        commande.setStatus(Commande.Status.EN_COURS);
        commande.setLivreur(null);
        commande.setDate(new Date());
        commande.setTotal(0L);
        commande.setTrackingID(UUID.randomUUID());
        commande.setPrix(0);
        
        // Initialiser les coordonnées client à des valeurs par défaut
        // Ces valeurs seront mises à jour lors de la validation de la commande
        commande.setClientLong(0.0);
        commande.setClientLalt(0.0);
        
        // Le livreur sera attribué plus tard lors de l'attribution automatique
        // La liste articles sera remplie automatiquement via les relations JPA
        
        return commandeRepository.save(commande);
    }
    
    public commandeInfo validerCommande(app.Backend.Dto.ValidationCommandeDto dto) {
        Optional<Commande> commandeOpt = commandeRepository.findByUtilisateurIdAndStatus(dto.getUserId(),Commande.Status.EN_COURS );
        if (commandeOpt.isEmpty()) throw new RuntimeException("Commande non trouvée");
        Commande commande = commandeOpt.get();

        // 2. Mettre à jour les coordonnées du client
        commande.setClientLong(dto.getClientLong());
        commande.setClientLalt(dto.getClientLat());

       
        Livreur livreur = livreurRepository.findLivreurLePlusProche(dto.getClientLong(), dto.getClientLat());
        commande.setLivreur(livreur);

        commande.setStatus(Commande.Status.EN_ATTENTE);
        commandeRepository.save(commande);

        
        commandeInfo info = new commandeInfo();
        info.setId(commande.getId());
        info.setPrix(commande.getPrix());
        info.setTotal(commande.getTotal());
        info.setStatus(commande.getStatus() != null ? commande.getStatus().name() : null);
        info.setClientLong(commande.getClientLong());
        info.setClientLalt(commande.getClientLalt());
        if (livreur != null && livreur.getLocation() != null) {
            
            info.setLivreurLong(livreur.getLocation().getY()); 
            info.setLivreurLat(livreur.getLocation().getX());  
        }
        return info;
    }
} 