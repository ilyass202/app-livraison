package app.Backend.model;

import app.Backend.Dto.ProduitDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    private String nom;
    private double prix;
    @Lob
    private String description;
    @Lob
    private String img;

    public ProduitDto getProduitDto(){
        ProduitDto dto = new ProduitDto();
        dto.setId(this.id);
        dto.setNom(this.nom);
        dto.setPrix(this.prix);
        dto.setDescription(this.description);
        dto.setImg(this.img);
        return dto;
    }

}
