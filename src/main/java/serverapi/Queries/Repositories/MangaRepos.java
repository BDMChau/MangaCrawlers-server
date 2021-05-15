package serverapi.Queries.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Tables.Manga.Manga;

import java.util.List;
import java.util.Optional;

@Repository
public interface MangaRepos extends JpaRepository<Manga, Long> {

    @Query("SELECT m FROM Manga m WHERE m.manga_name = ?1")
    Optional<Manga> findByName(String manga);

    @Query(value = "SELECT * FROM manga", nativeQuery = true)
    List<Manga> findAllManga();


    @Query(value = "SELECT author_id FROM manga,author where manga.author_id = author.author_id", nativeQuery = true)
    List<Manga> findAuthorId();
}
