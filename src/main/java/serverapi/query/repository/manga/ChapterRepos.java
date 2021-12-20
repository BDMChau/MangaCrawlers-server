package serverapi.query.repository.manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.ChapterDTO;
import serverapi.tables.manga_tables.chapter.Chapter;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepos extends JpaRepository<Chapter, Long> {


    @Query(value = "select * from chapter", nativeQuery = true)
    List<Chapter> findAllChapter();

//
//    @Query( value = "SELECT MAX(c.chapter_id), m.manga_name FROM Chapter c JOIN c.manga m WHERE c.manga = m.manga_id GROUP BY m.manga_name" )
//    List<Chapter> findmaxid();


    //    @Query(value = "SELECT SUM(c.views), m.manga_name from Chapter c  JOIN Manga m ON c.manga = m.manga_id GROUP BY m.manga_name")
//    List<Chapter> getTotalView();
    @Query("SELECT new serverapi.query.dtos.tables.ChapterDTO(c.chapter_id, c.chapter_name, c.created_at) FROM " +
           "Manga m JOIN m.chapters c " +
           "WHERE m.manga_id = ?1 ORDER BY c.chapter_id ")
    List<ChapterDTO> findChaptersbyMangaId(Long manga_id);

    @Query("""
            SELECT ch FROM Chapter ch JOIN Manga ma ON ch.manga.manga_id = ma.manga_id
            WHERE ch.chapter_id =(:chapter_id)
            AND ma.manga_id =(:manga_id)
                    """)
    Optional<Chapter> findChapterByMangaIdAndChapterId(@Param("chapter_id") Long chapter_id, @Param("manga_id") Long manga_id);

    @Query("SELECT new serverapi.query.dtos.tables.ChapterDTO(c.chapter_id, c.chapter_name, c.created_at, m.manga_id) FROM " +
           "Manga m JOIN m.chapters c")
    List<ChapterDTO> getAllChapter();


    @Query("""
            SELECT COUNT(c.chapter_id)
            FROM Manga m JOIN m.chapters c ON m.manga_id = c.manga.manga_id
            WHERE m.manga_id = ?1
            """)
    Long getTotalChaptersByMangaId(Long mangaId);

}
