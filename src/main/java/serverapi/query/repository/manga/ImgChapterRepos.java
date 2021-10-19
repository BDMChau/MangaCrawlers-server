package serverapi.query.repository.manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.ChapterImgDTO;
import serverapi.tables.manga_tables.image_chapter.ImageChapter;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImgChapterRepos extends JpaRepository<ImageChapter, Long> {

    @Query("SELECT new serverapi.query.dtos.tables.ChapterImgDTO(i.imgchapter_id, i.imgchapter_url) FROM Chapter c JOIN c" +
            ".imageChapters i WHERE c.chapter_id =?1 ")
    List<ChapterImgDTO> findImgsByChapterId(Long chapter_id);

    @Query("""
       SELECT new serverapi.query.dtos.tables.ChapterImgDTO(ch.chapter_id, ch.chapter_name, ma.manga_id, ma.manga_name)
       FROM Manga ma, Chapter ch
       WHERE ma.manga_id = ch.manga.manga_id
       AND ch.chapter_id = ?1
            """)
    Optional<ChapterImgDTO> getChapterID(Long chapter_id);
}
