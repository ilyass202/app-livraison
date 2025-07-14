package app.Backend.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date date;


    @ManyToOne
    private Livreur livreur;

    private double clientLong;
    private double clientLalt;
    private double prix;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        EN_ATTENTE, EN_COURS, LIVREE, ANNULEE
    }
    private Long total;

    private UUID trackingID;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "commande")
    @JsonManagedReference
    private List<Panier> articles = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id" , referencedColumnName = "id")
    private Utilisateur utilisateur;
}
