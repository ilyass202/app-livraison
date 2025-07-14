package app.Backend.Repositry;

import app.Backend.model.Livreur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LivreurRepositry extends JpaRepository<Livreur, Long> {
    @Query(value = "SELECT * FROM livreur ORDER BY ST_Distance_Sphere(ST_SRID(POINT(:clientLong, :clientLat), 4326), location) ASC LIMIT 1", nativeQuery = true)
    Livreur findLivreurLePlusProche(@Param("clientLong") double clientLong, @Param("clientLat") double clientLat);
}
