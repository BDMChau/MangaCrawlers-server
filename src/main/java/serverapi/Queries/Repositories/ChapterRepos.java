package serverapi.Queries.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.dto.LatestManga;

import java.util.List;

@Repository
public interface ChapterRepos extends JpaRepository<Chapter, Long> {


    @Query(value = "select * from chapter", nativeQuery = true)
    List<Chapter> findAllChapter();


    @Query( "SELECT (MAX( chapter_id) ) FROM Chapter" )
    List<Chapter> findmaxid();

}
