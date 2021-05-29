package serverapi.Query.Repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTO.*;
import serverapi.Tables.Manga.Manga;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Repository
public interface MangaRepos extends JpaRepository<Manga, Long>, JpaSpecificationExecutor<Manga> {

    @Query("SELECT m FROM Manga m WHERE m.manga_name = ?1")
    Optional<Manga> findByName(String manga);


//    @Query( "SELECT m FROM Manga m JOIN m.chapters c WHERE m.manga_id= :manga_id and c.chapter_id= :chapter_id")
//    Optional<Manga> findChapter(@Param("manga_id") Long manga_id, @Param("chapter_id") Long chapter_id);


    @Query("SELECT new serverapi.Query.DTO.MangaChapterDTO(c.chapter_id, c.chapter_name, c.createdAt, m.manga_id," +
            " m.manga_name, m.thumbnail) FROM Manga m JOIN m.chapters c WHERE c.chapter_id = (SELECT MAX(ct.chapter_id) FROM Manga mg INNER JOIN mg.chapters ct " +
            "WHERE mg.manga_id = m.manga_id ) Order by c.chapter_id Desc")
    List<MangaChapterDTO> getLatestChapterFromManga();


//    @Query(value = "select * from manga", nativeQuery = true)
//    List<Manga> getAllMangas();


    @Query(value = "SELECT * FROM Manga Order By views Desc limit :quantity", nativeQuery = true)
    List<Manga> getTop(@Param("quantity") Integer quantity);


    @Query("SELECT new serverapi.Query.DTO.MangaChapterGenreDTO(c.chapter_id, c.chapter_name, c.createdAt, m" +
            ".manga_id,m.manga_name, m.thumbnail, g.genre_id, g.genre_name, g.genre_description, g.genre_color) FROM " +
            "Manga m JOIN m.chapters c JOIN m.mangaGenres mg ON mg.manga = m.manga_id JOIN Genre g ON g.genre_id = mg.genre  " +
            "WHERE c.chapter_id = (SELECT MAX(ct.chapter_id) FROM Manga mg INNER JOIN mg.chapters ct WHERE mg.manga_id = m.manga_id " +
            "AND g.genre_id =?1) Order by c.chapter_id Desc")
    List<MangaChapterGenreDTO> findMangaByOneGenre(Long genre_id);


//    @Query("SELECT new serverapi.Query.DTO.TotalViews(c.views,m.manga_id,m.manga_name ) FROM  Manga m JOIN m
//    .chapters c
//    WHERE c.views = (SELECT SUM(ct.views)  FROM Manga mg INNER JOIN mg.chapters ct WHERE mg.manga_id = m.manga_id  ) GROUP BY c.views, m.manga_id,m.manga_name")
//    List<TotalViews> getTotalViews ();


    @Query(value = "SELECT new serverapi.Query.DTO.MangaViewDTO(Sum(c.views),m.manga_id,m.manga_name) FROM Manga m " +
            "JOIN m.chapters c GROUP BY m.manga_id, m.manga_name ")
    List<MangaViewDTO> getTotalView();


    @Query(value = "SELECT m FROM Manga m JOIN m.updateViews u WHERE u.createdAt >= current_date - :from_time and u.createdAt < current_date - :to_time Order By u" +
            ".totalviews Desc")
    List<Manga> getWeekly( @Param("from_time")int from_time, @Param("to_time") int to_time);


    @Query("SELECT new serverapi.Query.DTO.AuthorMangaDTO( a.author_id, a.author_name," +
            " m.manga_id, m.manga_name, m.status, m.description, m.stars, " +
            "m.views, m.thumbnail, m.date_publications, m.createdAt)" +
            " FROM Author a JOIN a.mangas m WHERE m.manga_id = ?1")
    Optional<AuthorMangaDTO> getAllByMangaId(Long manga_id);


    @Query("SELECT new serverapi.Query.DTO.AuthorMangaDTO( a.author_id, a.author_name," +
            " m.manga_id, m.manga_name, m.status, m.description, m.stars, " +
            "m.views, m.thumbnail, m.date_publications, m.createdAt)" +
            " FROM Author a JOIN a.mangas m")
    List<AuthorMangaDTO> getAllMangas();

    @Query("SELECT new serverapi.Query.DTO.AuthorMangaDTO( COUNT(c.chapter_id), a.author_id, a.author_name," +
            " m.manga_id, m.manga_name, m.status, m.description, m.stars, " +
            "m.views, m.thumbnail, m.date_publications, m.createdAt) " +
            "FROM Author a JOIN a.mangas m JOIN m.chapters c GROUP BY a.author_id, a.author_name, " +
            "m.manga_id, m.manga_name, m.status, m.description, m.stars, m.views, m.thumbnail, m.date_publications, m.createdAt")
    List<AuthorMangaDTO> getAllMangasInfo();

    @Query("SELECT new serverapi.Query.DTO.GenreDTO(g.genre_id, g.genre_name, g.genre_color, g.genre_description) " +
            "FROM" +
            " " +
            "Manga m JOIN m.mangaGenres mg ON mg.manga = m.manga_id JOIN Genre g ON g.genre_id = mg.genre  " +
            "WHERE m.manga_id =?1")
    List<GenreDTO> findGenresByMangId(Long manga_id);


    @Query("SELECT new serverapi.Query.DTO.ChapterDTO(c.chapter_id, c.chapter_name, c.createdAt) FROM " +
            "Manga m JOIN m.chapters c " +
            "WHERE m.manga_id = ?1")
    List<ChapterDTO> findChaptersbyMangaId(Long manga_id);



}
