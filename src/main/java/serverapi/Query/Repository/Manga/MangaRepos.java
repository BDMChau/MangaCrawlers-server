package serverapi.Query.Repository.Manga;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTOs.FeaturesDTOs.UpdateViewDTO;
import serverapi.Query.DTOs.TablesDTOs.*;
import serverapi.Tables.MangaTables.Manga.Manga;

import java.util.List;
import java.util.Optional;

@Repository
public interface MangaRepos extends JpaRepository<Manga, Long>, JpaSpecificationExecutor<Manga> {

    @Query("SELECT m FROM Manga m WHERE m.manga_name = ?1")
    Optional<Manga> findByName(String manga);


//    @Query( "SELECT m FROM Manga m JOIN m.chapters c WHERE m.manga_id= :manga_id and c.chapter_id= :chapter_id")
//    Optional<Manga> findChapter(@Param("manga_id") Long manga_id, @Param("chapter_id") Long chapter_id);


    @Query("SELECT new serverapi.Query.DTOs.TablesDTOs.MangaChapterDTO(c.chapter_id, c.chapter_name, c.created_at, m.manga_id," +
            " m.manga_name, m.thumbnail) FROM Manga m JOIN m.chapters c WHERE c.chapter_id = (SELECT MAX(ct.chapter_id) FROM Manga mg INNER JOIN mg.chapters ct " +
            "WHERE mg.manga_id = m.manga_id ) Order by c.chapter_id Desc")
    List<MangaChapterDTO> getLatestChapterFromManga();


    @Query("SELECT new serverapi.Query.DTOs.TablesDTOs.MangaChapterDTO(c.chapter_id, c.chapter_name, c.created_at, m.manga_id," +
            " m.manga_name, m.thumbnail) FROM Manga m JOIN m.chapters c WHERE c.chapter_id = (SELECT MAX(ct.chapter_id) FROM Manga mg INNER JOIN mg.chapters ct " +
            "WHERE mg.manga_id = m.manga_id ) AND m.manga_id =?1 Order by c.chapter_id Desc")
    Optional<MangaChapterDTO> getLatestChapterFromMangaByMangaId(Long manga_id);


    @Query("SELECT new serverapi.Query.DTOs.TablesDTOs.MangaChapterDTO(c.chapter_id, c.chapter_name, c.created_at, m.manga_id," +
            " m.manga_name, m.thumbnail) FROM Manga m JOIN m.chapters c JOIN TransGroup tg ON tg.transgroup_id = m.transgroup WHERE c.chapter_id = (SELECT MAX(ct.chapter_id) FROM Manga mg INNER JOIN mg.chapters ct " +
            "WHERE mg.manga_id = m.manga_id ) AND tg.transgroup_id =?1 Order by c.chapter_id Desc")
    List<MangaChapterDTO> getLatestChapterFromMangaByTransgroup(Long transgroup_id);


    @Query("SELECT new serverapi.Query.DTOs.TablesDTOs.MangaChapterDTO(m.manga_id," +
            " m.manga_name, m.thumbnail, m.stars) FROM Manga m JOIN TransGroup tg ON tg.transgroup_id = m.transgroup WHERE tg.transgroup_id =?1")
    default List<MangaChapterDTO> getMangaByTransgroup(Long transgroup_id) {
        return null;
    }


    @Query(value = "SELECT * FROM Manga ORDER BY RANDOM() LIMIT :quantity", nativeQuery = true)
    List<Manga> getRandomList(@Param("quantity") Integer quantity);


    long count();

    Page<Manga> findAll(Pageable pageable);

//    @Query(value = "select * from manga", nativeQuery = true)
//    List<Manga> getAllMangas();


    @Query(value = "SELECT * FROM Manga Order By views Desc limit :quantity", nativeQuery = true)
    List<Manga> getTop(@Param("quantity") Integer quantity);


    @Query("SELECT new serverapi.Query.DTOs.TablesDTOs.MangaChapterGenreDTO(c.chapter_id, c.chapter_name, c.created_at, m" +
            ".manga_id,m.manga_name, m.thumbnail, g.genre_id, g.genre_name, g.genre_description, g.genre_color) FROM " +
            "Manga m JOIN m.chapters c JOIN m.mangaGenres mg ON mg.manga = m.manga_id JOIN Genre g ON g.genre_id = mg.genre  " +
            "WHERE c.chapter_id = (SELECT MAX(ct.chapter_id) FROM Manga mg INNER JOIN mg.chapters ct WHERE mg.manga_id = m.manga_id " +
            "AND g.genre_id =?1) Order by c.chapter_id Desc")
    List<MangaChapterGenreDTO> findMangaByOneGenre(Long genre_id);


//    @Query("SELECT new serverapi.Query.DTO.TotalViews(c.views,m.manga_id,m.manga_name ) FROM  Manga m JOIN m
//    .chapters c
//    WHERE c.views = (SELECT SUM(ct.views)  FROM Manga mg INNER JOIN mg.chapters ct WHERE mg.manga_id = m.manga_id  ) GROUP BY c.views, m.manga_id,m.manga_name")
//    List<TotalViews> getTotalViews ();


    @Query(value = "SELECT new serverapi.Query.DTOs.TablesDTOs.MangaViewDTO(Sum(c.views),m.manga_id,m.manga_name) FROM Manga m " +
            "JOIN m.chapters c GROUP BY m.manga_id, m.manga_name ")
    List<MangaViewDTO> getTotalView();


//    @Query("SELECT new serverapi.Query.DTO.UpdateViewDTO(m.manga_id,m.manga_name, m.thumbnail, m.description, m.status, m.stars," +
//            "m.views, m.date_publications, m.created_at, u.updatedview_id, u.totalviews, u.created_at) " +
//            "FROM UpdateView u JOIN u.manga m " +
//            "WHERE u.created_at >= (current_date - (:from_time )) and u.created_at <= (current_date - (:to_time)) ")
//    List<UpdateViewDTO> getWeekly(@Param("from_time") int from_time, @Param("to_time") int to_time);


    @Query("SELECT new serverapi.Query.DTOs.TablesDTOs.UpdateViewDTO(m.manga_id,m.manga_name, m.thumbnail, m.description, m.status, m.stars," +
            "m.views, m.date_publications, m.created_at, u.updatedview_id, u.totalviews, u.created_at) " +
            "FROM UpdateView u JOIN u.manga m " +
            "WHERE DATE(u.created_at) >= (CURRENT_DATE - :from_time) and DATE(u.created_at) < (CURRENT_DATE - :to_time)")
    List<UpdateViewDTO> getWeekly(@Param("from_time") int from_time, @Param("to_time") int to_time);



    @Query("SELECT new serverapi.Query.DTOs.TablesDTOs.AuthorMangaDTO( a.author_id, a.author_name," +
            " m.manga_id, m.manga_name, m.status, m.description, m.stars, " +
            "m.views, m.thumbnail, m.date_publications, m.created_at)" +
            " FROM Author a JOIN a.mangas m WHERE m.manga_id = ?1")
    Optional<AuthorMangaDTO> getAllByMangaId(Long manga_id);



    @Query("SELECT new serverapi.Query.DTOs.TablesDTOs.AuthorMangaDTO( a.author_id, a.author_name," +
            " m.manga_id, m.manga_name, m.status, m.description, m.stars, " +
            "m.views, m.thumbnail, m.date_publications, m.created_at)" +
            " FROM Author a JOIN a.mangas m")
    List<AuthorMangaDTO> getAllMangas();

    @Query("SELECT new serverapi.Query.DTOs.TablesDTOs.AuthorMangaDTO( COUNT(c.chapter_id), a.author_id, a.author_name," +
            " m.manga_id, m.manga_name, m.status, m.description, m.stars, " +
            "m.views, m.thumbnail, m.date_publications, m.created_at) " +
            "FROM Author a JOIN a.mangas m JOIN m.chapters c GROUP BY a.author_id, a.author_name, " +
            "m.manga_id, m.manga_name, m.status, m.description, m.stars, m.views, m.thumbnail, m.date_publications, m.created_at")
    List<AuthorMangaDTO> getAllMangasInfo();

    @Query("SELECT new serverapi.Query.DTOs.TablesDTOs.GenreDTO(g.genre_id, g.genre_name, g.genre_color, g.genre_description) " +
            "FROM" +
            " " +
            "Manga m JOIN m.mangaGenres mg ON mg.manga = m.manga_id JOIN Genre g ON g.genre_id = mg.genre  " +
            "WHERE m.manga_id =?1")
    List<GenreDTO> findGenresByMangId(Long manga_id);


    @Query("SELECT new serverapi.Query.DTOs.TablesDTOs.ChapterDTO(c.chapter_id, c.chapter_name, c.created_at) FROM " +
            "Manga m JOIN m.chapters c " +
            "WHERE m.manga_id = ?1")
    List<ChapterDTO> findChaptersbyMangaId(Long manga_id);


}
