package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTO.ChapterDTO;
import serverapi.Query.DTO.MangaDTO;
import serverapi.Tables.User.User;

import java.util.List;

@Repository
public interface UserRepos extends JpaRepository<User, Long> {

    @Query("SELECT new serverapi.Query.DTO.ChapterDTO(c.chapter_id, c.chapter_name, c.createdAt) FROM Chapter c JOIN c.readingHistories rd WHERE c.chapter_id =?1 ")
    List<ChapterDTO> findChapterByReadingHistory(Long chapter_id);

    @Query("SELECT new serverapi.Query.DTO.MangaDTO(m.manga_id, m.manga_name, m.thumbnail) FROM Manga m JOIN m.readingHistories rd WHERE m.manga_id =?1 ")
    List<MangaDTO> findMangaByReadingHistory(Long manga_id);


}

