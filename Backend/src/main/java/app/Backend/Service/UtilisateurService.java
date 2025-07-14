package app.Backend.Service;


import java.util.Date;
import java.util.UUID;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import app.Backend.Repositry.CommandeRepositry;
import app.Backend.Repositry.UtilisateurRepositry;
import app.Backend.model.Commande;
import app.Backend.model.Utilisateur;


@Service
public class UtilisateurService implements UserDetailsService {
    private UtilisateurRepositry repositry;
    private PasswordEncoder encoder;

    public UtilisateurService(UtilisateurRepositry repositry , PasswordEncoder encoder){
        this.repositry = repositry;
        this.encoder = encoder;
    }
    public Utilisateur saveUtilisateur(Utilisateur utilisateur){
        utilisateur.setPassword(encoder.encode((utilisateur.getPassword())));
        Utilisateur saved = repositry.save(utilisateur);
        return saved;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = repositry.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));
        return User.builder().username(utilisateur.getEmail()).password(utilisateur.getPassword()).build();
    }

}
