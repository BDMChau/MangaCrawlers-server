package serverapi.Query.Repository.Manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTOs.TablesDTOs.ChapterDTO;
import serverapi.Tables.MangaTables.Chapter.Chapter;

import java.util.List;

@Repository
public interface ChapterRepos extends JpaRepository<Chapter, Long> {


    @Query(value = "select * from chapter", nativeQuery = true)
    List<Chapter> findAllChapter();

//
//    @Query( value = "SELECT MAX(c.chapter_id), m.manga_name FROM Chapter c JOIN c.manga m WHERE c.manga = m.manga_id GROUP BY m.manga_name" )
//    List<Chapter> findmaxid();



//    @Query(value = "SELECT SUM(c.views), m.manga_name from Chapter c  JOIN Manga m ON c.manga = m.manga_id GROUP BY m.manga_name")
//    List<Chapter> getTotalView();
@Query("SELECT new serverapi.Query.DTO.ChapterDTO(c.chapter_id, c.chapter_name, c.createdAt) FROM " +
        "Manga m JOIN m.chapters c " +
        "WHERE m.manga_id = ?1")
List<ChapterDTO> findChaptersbyMangaId(Long manga_id);

    @Query("SELECT new serverapi.Query.DTO.ChapterDTO(c.chapter_id, c.chapter_name, c.createdAt, m.manga_id) FROM " +
            "Manga m JOIN m.chapters c")
    List<ChapterDTO> getAllChapter();





}
