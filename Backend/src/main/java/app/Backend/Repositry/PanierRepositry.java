package app.Backend.Repositry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.Backend.model.Panier;

@Repository
public interface PanierRepositry extends JpaRepository<Panier , Long> {
     Optional<Panier> findByProduitIdAndCommandeIdAndUtilisateurId(Long produitId , Long commandeId , Long userId);
}
