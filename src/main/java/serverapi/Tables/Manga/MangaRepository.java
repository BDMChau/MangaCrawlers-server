package serverapi.Tables.Manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Long> {

    @Query("SELECT m FROM Manga m WHERE m.manga_name = ?1")
    Optional<Manga> findByName(String manga);

}
