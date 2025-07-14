package app.Backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.Backend.Dto.CommandeEnvoyeDto;
import app.Backend.Dto.PanierDto;
import app.Backend.Service.PanierService;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;


@RestController
@RequestMapping("client")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PanierController {
    private final PanierService panierservice;

    @PostMapping("/Panier")
    public ResponseEntity<?> panierCreer(@RequestBody PanierDto panierDto) {
       return panierservice.ajouterPanier(panierDto);        
    }

    @PostMapping("/Panier/augmenter")
    public ResponseEntity<?> augmenterQuantite(@RequestBody PanierDto panierDto) {
        CommandeEnvoyeDto commande = panierservice.augmenterQuantite(panierDto);
        return ResponseEntity.status(HttpStatus.OK).body(commande);
    }

    @PostMapping("/Panier/diminuer")
    public ResponseEntity<?> diminuerQuantite(@RequestBody PanierDto panierDto) {
        CommandeEnvoyeDto commande = panierservice.diminuerQuantite(panierDto);
        return ResponseEntity.status(HttpStatus.OK).body(commande);
    }

    @DeleteMapping("/Panier/{panierId}/{userId}")
    public ResponseEntity<?> supprimerArticle(@PathVariable Long panierId, @PathVariable Long userId) {
        CommandeEnvoyeDto commande = panierservice.supprimerArticle(panierId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(commande);
    }

    @GetMapping("/Panier/{userId}")
    public ResponseEntity<?> getMethodName(@PathVariable Long userId) {
        CommandeEnvoyeDto commande = panierservice.getCommandeById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(commande);
    }
    
    
}
