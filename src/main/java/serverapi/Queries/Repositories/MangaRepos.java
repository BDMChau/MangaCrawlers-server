package serverapi.Queries.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.Queries.DTO.MangaViewDTO;
import serverapi.Tables.Manga.Manga;
import serverapi.Queries.DTO.MangaChapterDTO;

import java.util.List;
import java.util.Optional;

@Repository
public interface MangaRepos extends JpaRepository<Manga, Long> {

    @Query("SELECT m FROM Manga m WHERE m.manga_name = ?1")
    Optional<Manga> findByName(String manga);


//    @Query( "SELECT m FROM Manga m JOIN m.chapters c WHERE m.manga_id= :manga_id and c.chapter_id= :chapter_id")
//    Optional<Manga> findChapter(@Param("manga_id") Long manga_id, @Param("chapter_id") Long chapter_id);


    @Query("SELECT new serverapi.Queries.DTO.MangaChapterDTO(c.chapter_id, c.chapter_name, c.createdAt, m.manga_id, m" +
            ".manga_name, m.thumbnail) FROM " +
            "Manga m JOIN m.chapters c WHERE c.chapter_id = (SELECT MAX(ct.chapter_id) FROM Manga mg INNER JOIN mg.chapters ct WHERE mg.manga_id = m.manga_id ) Order by c.chapter_id Desc")
    List<MangaChapterDTO> getLatestChapterFromManga();


    @Query(value = "select * from manga", nativeQuery = true)
    List<Manga> getAllMangas();


    @Query(value = "SELECT * FROM Manga Order By views Desc limit :quantity", nativeQuery = true)
    List<Manga> getTop(@Param("quantity") Integer quantity);



//    @Query("SELECT new serverapi.Queries.DTO.TotalViews(c.views,m.manga_id,m.manga_name ) FROM  Manga m JOIN m.chapters c WHERE c.views = (SELECT SUM(ct.views)  FROM Manga mg INNER JOIN mg.chapters ct WHERE mg.manga_id = m.manga_id  ) GROUP BY c.views, m.manga_id,m.manga_name")
//    List<TotalViews> getTotalViews ();


    @Query(value ="SELECT new serverapi.Queries.DTO.MangaViewDTO(Sum(c.views),m.manga_id,m.manga_name) FROM Manga m JOIN m.chapters c  GROUP BY m.manga_id,m.manga_name ")
    List<MangaViewDTO> getTotalView();


    @Query(value = "SELECT m FROM Manga m JOIN m.updateViews u WHERE u.createdAt > current_date - 7    Order By u.totalviews Desc ")
    List<Manga> getWeeklyTop();

}
