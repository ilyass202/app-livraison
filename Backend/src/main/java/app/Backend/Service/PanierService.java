package app.Backend.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import app.Backend.Dto.CommandeEnvoyeDto;
import app.Backend.Dto.PanierDto;
import app.Backend.Repositry.CommandeRepositry;
import app.Backend.Repositry.PanierRepositry;
import app.Backend.Repositry.ProduitRepositry;
import app.Backend.Repositry.UtilisateurRepositry;
import app.Backend.model.Commande;
import app.Backend.model.Panier;
import app.Backend.model.Produit;
import app.Backend.model.Utilisateur;

import java.util.List;

@Service
public class PanierService {
    @Autowired
    private CommandeRepositry commandeRepository;
    @Autowired
    private UtilisateurRepositry utilisateurRepository;
    @Autowired 
    private PanierRepositry panierRepository;
    @Autowired
    private ProduitRepositry produitRepository;
    @Autowired
    private CommandeService commandeService;

    public ResponseEntity<?> ajouterPanier(PanierDto dto) {
        // 1. Récupérer ou créer la commande en cours de l'utilisateur
        Optional<Commande> commandeOpt = commandeRepository.findByUtilisateurIdAndStatus(dto.getUserId(), Commande.Status.EN_COURS);
        Commande commandeActive = commandeOpt.orElse(null);
        if (commandeActive == null) {
            
            try {
                commandeActive = commandeService.creerCommande(dto.getUserId());
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
            }
        }

        // 2. Vérifier si le produit est déjà dans le panier pour cette commande et cet utilisateur
        Optional<Panier> optionPanier = panierRepository.findByProduitIdAndCommandeIdAndUtilisateurId(
            dto.getProduitId(), commandeActive.getId(), dto.getUserId()
        );
        if (optionPanier.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Produit déjà dans le panier.");
        }

        // 3. Vérifier l'existence du produit et de l'utilisateur
        Optional<Produit> produitOpt = produitRepository.findById(dto.getProduitId());
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(dto.getUserId());
        if (produitOpt.isEmpty() || utilisateurOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur ou produit non trouvé.");
        }

        // 4. Créer et sauvegarder le panier
        Panier panierCreer = new Panier();
        panierCreer.setProduit(produitOpt.get());
        panierCreer.setPrix((long) produitOpt.get().getPrix());
        panierCreer.setQuantite(1);
        panierCreer.setUtilisateur(utilisateurOpt.get());
        panierCreer.setCommande(commandeActive);
        Panier panierSauvegarde = panierRepository.save(panierCreer);

        // 5. Mettre à jour le total de la commande
        commandeActive.setTotal(commandeActive.getTotal() + panierCreer.getPrix());
        commandeActive.setPrix(commandeActive.getPrix() + panierCreer.getPrix());
        commandeActive.getArticles().add(panierCreer);


        commandeRepository.save(commandeActive);

        return ResponseEntity.status(HttpStatus.CREATED).body(panierSauvegarde);

    }
    public CommandeEnvoyeDto getCommandeById(Long userId){
        Optional<Commande> commandeOpt = commandeRepository.findByUtilisateurIdAndStatus(userId, Commande.Status.EN_COURS);

        if (commandeOpt.isEmpty()) {
            return null;
        }

        Commande commande = commandeOpt.get();
        // Convertir les articles en ProduitPanierDto
        List<app.Backend.Dto.ProduitPanierDto> panierDtos = commande.getArticles() != null ?
            commande.getArticles().stream().map(Panier::getPanierDto).collect(java.util.stream.Collectors.toList()) : new java.util.ArrayList<>();

        CommandeEnvoyeDto dto = new CommandeEnvoyeDto();
        dto.setId(commande.getId());
        dto.setPrix(commande.getPrix());
        dto.setTotal(commande.getTotal());
        dto.setStatus(commande.getStatus() != null ? commande.getStatus().name() : null);
        dto.setClientLong(commande.getClientLong());
        dto.setClientLalt(commande.getClientLalt());
        dto.setArticles(panierDtos);
        // Remplir les infos utilisateur si besoin
        if (commande.getUtilisateur() != null) {
            dto.setUtilisateurId(commande.getUtilisateur().getId());
            dto.setUtilisateurNom(commande.getUtilisateur().getNom());
            dto.setUtilisateurEmail(commande.getUtilisateur().getEmail());
        }
        return dto;
    }
    public CommandeEnvoyeDto augmenterQuantite(PanierDto dto) {
    // 1. Récupérer la commande en cours de l'utilisateur
    Optional<Commande> commandeOpt = commandeRepository.findByUtilisateurIdAndStatus(dto.getUserId(), Commande.Status.EN_COURS);
    if (commandeOpt.isEmpty()) {
        throw new RuntimeException("Commande en cours non trouvée pour l'utilisateur.");
    }
    Commande commande = commandeOpt.get();

    // 2. Récupérer le produit concerné
    Optional<Produit> produitOpt = produitRepository.findById(dto.getProduitId());
    if (produitOpt.isEmpty()) {
        throw new RuntimeException("Produit non trouvé.");
    }
    Produit produit = produitOpt.get();

    // 3. Récupérer la ligne de panier correspondante
    Optional<Panier> panierOpt = panierRepository.findByProduitIdAndCommandeIdAndUtilisateurId(
        dto.getProduitId(), commande.getId(), dto.getUserId()
    );
    if (panierOpt.isEmpty()) {
        throw new RuntimeException("Produit non présent dans le panier.");
    }
    Panier panier = panierOpt.get();

    // 4. Augmenter la quantité et mettre à jour le prix de la ligne
    int nouvelleQuantite = panier.getQuantite() + 1;
    panier.setQuantite(nouvelleQuantite);
    panier.setPrix((long) (produit.getPrix() * nouvelleQuantite));
    panierRepository.save(panier);

    // 5. Recalculer le total de la commande (somme des prix de tous les paniers)
    long nouveauTotal = commande.getArticles().stream()
        .mapToLong(Panier::getPrix)
        .sum();
    commande.setTotal(nouveauTotal);
    commande.setPrix(nouveauTotal); // Si tu utilises aussi le champ prix
    commandeRepository.save(commande);

    // 6. Retourner le DTO à jour
    return getCommandeById(dto.getUserId());
}
    public CommandeEnvoyeDto diminuerQuantite(PanierDto dto) {
        // 1. Récupérer la commande en cours de l'utilisateur
        Optional<Commande> commandeOpt = commandeRepository.findByUtilisateurIdAndStatus(dto.getUserId(), Commande.Status.EN_COURS);
        if (commandeOpt.isEmpty()) {
            throw new RuntimeException("Commande en cours non trouvée pour l'utilisateur.");
        }
        Commande commande = commandeOpt.get();

        // 2. Récupérer le produit concerné
        Optional<Produit> produitOpt = produitRepository.findById(dto.getProduitId());
        if (produitOpt.isEmpty()) {
            throw new RuntimeException("Produit non trouvé.");
        }
        Produit produit = produitOpt.get();

        // 3. Récupérer la ligne de panier correspondante
        Optional<Panier> panierOpt = panierRepository.findByProduitIdAndCommandeIdAndUtilisateurId(
            dto.getProduitId(), commande.getId(), dto.getUserId()
        );
        if (panierOpt.isEmpty()) {
            throw new RuntimeException("Produit non présent dans le panier.");
        }
        Panier panier = panierOpt.get();

        // 4. Diminuer la quantité ou supprimer la ligne si quantité == 1
        if (panier.getQuantite() > 1) {
            int nouvelleQuantite = panier.getQuantite() - 1;
            panier.setQuantite(nouvelleQuantite);
            panier.setPrix((long) (produit.getPrix() * nouvelleQuantite));
            panierRepository.save(panier);
        } else {
            panierRepository.deleteById(panier.getId());
        }

        // 5. Recalculer le total de la commande (somme des prix de tous les paniers)
        long nouveauTotal = commande.getArticles().stream()
            .mapToLong(Panier::getPrix)
            .sum();
        commande.setTotal(nouveauTotal);
        commande.setPrix(nouveauTotal); // Si tu utilises aussi le champ prix
        commandeRepository.save(commande);

        // 6. Retourner le DTO à jour
        return getCommandeById(dto.getUserId());
    }

    public CommandeEnvoyeDto supprimerArticle(Long panierId, Long userId) {
        // 1. Récupérer la commande en cours de l'utilisateur
        Optional<Commande> commandeOpt = commandeRepository.findByUtilisateurIdAndStatus(userId, Commande.Status.EN_COURS);
        if (commandeOpt.isEmpty()) {
            throw new RuntimeException("Commande en cours non trouvée pour l'utilisateur.");
        }
        Commande commande = commandeOpt.get();

        // 2. Supprimer la ligne de panier
        panierRepository.deleteById(panierId);

        // 3. Recalculer le total de la commande (somme des prix de tous les paniers)
        long nouveauTotal = commande.getArticles().stream()
            .filter(p -> !p.getId().equals(panierId))
            .mapToLong(Panier::getPrix)
            .sum();
        commande.setTotal(nouveauTotal);
        commande.setPrix(nouveauTotal); // Si tu utilises aussi le champ prix
        commandeRepository.save(commande);

        // 4. Retourner le DTO à jour
        return getCommandeById(userId);
    }
}
