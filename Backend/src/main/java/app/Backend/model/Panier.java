package app.Backend.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import app.Backend.Dto.ProduitPanierDto;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
public class Panier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    private Long prix;

    private int quantite ;
    
    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "produit_id" , nullable = false )
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private Produit produit ;

    @ManyToOne(fetch = FetchType.LAZY , optional = false)
    @JoinColumn(name = "utilisateur_id" , nullable = false )
    @org.hibernate.annotations.OnDelete(action = org.hibernate.annotations.OnDeleteAction.CASCADE)
    private Utilisateur utilisateur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id")
    @JsonBackReference
    private Commande commande ;
    
    public ProduitPanierDto getPanierDto(){
        ProduitPanierDto panier = new ProduitPanierDto();
        panier.setId(id);
        panier.setPrix(prix);
        panier.setProduitId(produit.getId());
        panier.setQuantite(quantite);
        panier.setProduitNom(produit.getNom());
        panier.setImg(produit.getImg());
        return panier ; 
    }
}
