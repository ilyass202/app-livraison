package app.Backend.Dto;


import lombok.Data;

@Data
public class ProduitDto {

    private Long id ;
    private String nom;
    private double prix;
    private String description;
    private String img;

}
