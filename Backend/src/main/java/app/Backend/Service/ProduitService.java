package app.Backend.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.Backend.Dto.ProduitDto;
import app.Backend.Repositry.ProduitRepositry;
import app.Backend.model.Produit;

@Service
public class ProduitService {
    @Autowired
    private ProduitRepositry repo;

    public List<ProduitDto> getProduits(){
        try {
            List<Produit> produits = repo.findAll();
            return produits.stream().map(Produit::getProduitDto).collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of(); // Retourne une liste vide en cas d'erreur
        }
    }
    public List<ProduitDto> getProduitNom(String nom){
        List<Produit> produits = repo.findAllByNomContaining(nom);
        return produits.stream().map(Produit::getProduitDto).collect(Collectors.toList());
    }
}
