package app.Backend.Repositry;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import app.Backend.model.Utilisateur;

public interface UtilisateurRepositry extends JpaRepository<Utilisateur , Long>
{
    Optional<Utilisateur> findByEmail(String email);

}

