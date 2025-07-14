package app.Backend.Dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class CommandeEnvoyeDto {
    private Long id;
    private Date date;

    // Utiliser un type simple ou un DTO pour Livreur

    private double clientLong;
    private double clientLalt;
    private double prix;

    // Utiliser une simple String pour le statut
    private String status;

    private Long total;
    private UUID trackingID;

    private List<ProduitPanierDto> articles = new ArrayList<>();

    // Utiliser un type simple ou un DTO pour Utilisateur
    private Long utilisateurId;
    private String utilisateurNom;
    private String utilisateurEmail;
}
