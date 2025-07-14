package app.Backend.Repositry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.Backend.model.Commande;

@Repository
public interface CommandeRepositry extends JpaRepository<Commande , Long> {
    Optional<Commande> findByUtilisateurIdAndStatus(Long UtilisateurId , Commande.Status status);
}
