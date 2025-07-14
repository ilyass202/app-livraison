package app.Backend.controller;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.Backend.Repositry.UtilisateurRepositry;
import app.Backend.Service.UtilisateurService;
import app.Backend.model.Utilisateur;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class RegisterLoginController {
    private final UtilisateurService utilisateurService;
    private final UtilisateurRepositry repo;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Utilisateur utilisateur){
        Optional<Utilisateur> utili = repo.findByEmail(utilisateur.getEmail());
        if (utili.isPresent()){
            return ResponseEntity.badRequest().body("Email déjà utilisé");
        }
        Utilisateur utilisaved = utilisateurService.saveUtilisateur(utilisateur);
        return ResponseEntity.ok(utilisaved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String utilisateurActuel = authentication.getName();
        Optional<Utilisateur> utili = repo.findByEmail(utilisateurActuel);
        if (utili.isPresent()) {
            return ResponseEntity.ok(utili.get());
        } else {
            return ResponseEntity.badRequest().body("Vous n'êtes pas dans la liste des enregistrements");
        }
       
}
@PostMapping("/public")
public String publicEndpoint() {
return "OK";
}
}
