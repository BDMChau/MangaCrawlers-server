package serverapi.Queries.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.dto.LatestManga;

import java.util.List;
import java.util.Optional;

@Repository
public interface MangaRepos extends JpaRepository<Manga, Long> {

    @Query("SELECT m FROM Manga m WHERE m.manga_name = ?1")
    Optional<Manga> findByName(String manga);

    @Query(value = "SELECT m FROM Manga m WHERE m.manga_name = ?1", nativeQuery = true)
    List<Manga> findMangabyId();

//
//    @Query("SELECT a FROM manga m, author a where m.author_id = a.author_id")
//    List<Manga> findAuthorId();

//
//    @Query( "SELECT new serverapi.Tables.dto.LatestManga (MAX( c.chapter_id) ) FROM Manga m JOIN m.chapters c GROUP BY m.manga_name " )
//    List<LatestManga> getlatestmanga();

}
