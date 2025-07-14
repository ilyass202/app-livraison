package app.Backend.controller;

import java.util.List;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.Backend.Dto.ProduitDto;
import app.Backend.Service.ProduitService;



@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:4200")
public class ProduitController {
   private final ProduitService produit;
   
   public ProduitController(ProduitService produit){
      this.produit = produit;
   }

    @GetMapping("/produits")
    public ResponseEntity<List<ProduitDto>> getProduits(){
        List<ProduitDto> produits = produit.getProduits();
        return ResponseEntity.ok(produits);
    }
    @GetMapping("/search/{nom}")
    public ResponseEntity<List<ProduitDto>> getProduitNom(@PathVariable String nom){
       List<ProduitDto> produitsDto = produit.getProduitNom(nom);
       return ResponseEntity.ok(produitsDto);
    }
  
    
}
