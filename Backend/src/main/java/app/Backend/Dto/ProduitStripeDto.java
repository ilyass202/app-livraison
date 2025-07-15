package app.Backend.Dto;

import lombok.Data;

@Data
public class ProduitStripeDto {
    private String nom;
    private Long prix; // en centimes
    private int quantite;
}