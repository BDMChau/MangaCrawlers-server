package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTO.UserReadingHistoryDTO;
import serverapi.Tables.ReadingHistory.ReadingHistory;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingHistoryRepos extends JpaRepository<ReadingHistory, Long> {

    @Query("SELECT new serverapi.Query.DTO.UserReadingHistoryDTO(rd.readingHistory_id,rd.reading_history_time,u.user_id, " +
            "m.manga_id,m.manga_name,m.thumbnail,m.stars,m.views,m. date_publications,m.status," +
            "c.chapter_id, c.chapter_name,c.createdAt) " +
            " FROM ReadingHistory rd JOIN User u ON rd.user = u.user_id JOIN Manga m ON rd.manga = m.manga_id JOIN Chapter c ON rd.chapter = c.chapter_id WHERE u.user_id =?1  ")
    List<UserReadingHistoryDTO> GetUserByReadingHistory(Long user_id);


}
