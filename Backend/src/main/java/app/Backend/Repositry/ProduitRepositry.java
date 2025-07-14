package app.Backend.Repositry;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import app.Backend.model.Produit;

@Repository
public interface ProduitRepositry extends JpaRepository<Produit , Long>{

    List<Produit> findAllByNomContaining(String title);

}

