package serverapi.query.repository.manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.ChapterImgDTO;
import serverapi.tables.manga_tables.image_chapter.ImageChapter;

import java.util.List;

@Repository
public interface ImgChapterRepos extends JpaRepository<ImageChapter, Long> {

    @Query("SELECT new serverapi.query.dtos.tables.ChapterImgDTO(i.imgchapter_id, i.imgchapter_url) FROM Chapter c JOIN c" +
            ".imageChapters i WHERE c.chapter_id =?1 ")
    List<ChapterImgDTO> findImgsByChapterId(Long chapter_id);

    @Query("SELECT ic FROM ImageChapter ic JOIN Chapter c" +
           " ON ic.chapter.chapter_id = c.chapter_id WHERE c.chapter_id =?1 ")
    void deleteImageChapterByChapterId(Long chapter_id);


}
