package app.Backend.Dto;

import lombok.Data;

@Data
public class ProduitPanierDto {
    private Long id ;

    private Long prix;

    private int quantite ;

    private Long produitId;

    private Long commandeId;

    private String produitNom;

    private String img;
    
    private Long userId;
}
