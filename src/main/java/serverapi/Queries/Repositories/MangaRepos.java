package serverapi.Queries.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.dto.LatestManga;
import serverapi.Tables.dto.SumViewChapter;

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



    @Query( "SELECT new serverapi.Tables.dto.LatestManga( c.chapter_id, m.manga_name) FROM Manga m  JOIN m.chapters c WHERE c.chapter_id = (SELECT MAX(ct.chapter_id) FROM Manga mg INNER JOIN mg.chapters ct WHERE mg.manga_id = m.manga_id) " )
    List<LatestManga> getLatestManga();


    @Query(value = "select * from manga",nativeQuery = true)
    List<Manga> getAllManga();

//    @Query("SELECT new serverapi.Tables.dto.SumViewChapter(TOP 5 m.manga_name,m.manga_id, m.views) FROM Manga m ORDER BY m.views DESC")
//    List<Manga> gettopmanga();

    @Query(value = "SELECT * FROM Manga Order By views Desc limit 5",nativeQuery = true)
    List<Manga> getTopManga();





}
