package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTO.ChapterImgDTO;
import serverapi.Tables.MangaTables.ImageChapter.ImageChapter;

import java.util.List;

@Repository
public interface ImgChapterRepos extends JpaRepository<ImageChapter, Long> {

    @Query("SELECT new serverapi.Query.DTO.ChapterImgDTO(i.imgchapter_id, i.imgchapter_url) FROM Chapter c JOIN c" +
            ".imageChapters i WHERE c.chapter_id =?1 ")
    List<ChapterImgDTO> findImgsByChapterId(Long chapter_id);


}
