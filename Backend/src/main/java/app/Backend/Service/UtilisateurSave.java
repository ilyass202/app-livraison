package app.Backend.Service;



import java.util.Date;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.Backend.Repositry.CommandeRepositry;
import app.Backend.Repositry.UtilisateurRepositry;
import app.Backend.model.Commande;
import app.Backend.model.Utilisateur;

@Service
public class UtilisateurSave {
    private UtilisateurRepositry repositry;
    private PasswordEncoder encoder;
    private CommandeRepositry repositry2;

    public UtilisateurSave(UtilisateurRepositry repositry , PasswordEncoder encoder, CommandeRepositry repositry2){
        this.repositry = repositry;
        this.encoder = encoder;
        this.repositry2 = repositry2;
    }
    public Utilisateur saveUtilisateur(Utilisateur utilisateur){
        utilisateur.setPassword(encoder.encode((utilisateur.getPassword())));
        Utilisateur saved = repositry.save(utilisateur);
        Commande commande = new Commande();
        commande.setTotal((long) 0.0);
        commande.setUtilisateur(saved);
        commande.setStatus(Commande.Status.EN_ATTENTE);
        commande.setDate(new Date());
        commande.setTrackingID(UUID.randomUUID());
        repositry2.save(commande);

        return saved;
        

    }

}
