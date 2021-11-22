package serverapi.query.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.UserReadingHistoryDTO;
import serverapi.tables.user_tables.following_manga.FollowingManga;
import serverapi.tables.user_tables.reading_history.ReadingHistory;
import serverapi.tables.user_tables.user.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReadingHistoryRepos extends JpaRepository<ReadingHistory, Long> {

    @Query("SELECT new serverapi.query.dtos.tables.UserReadingHistoryDTO(rd.readingHistory_id,rd.reading_history_time,u.user_id, " +
            "m.manga_id,m.manga_name,m.thumbnail,m.stars,m.views,m. date_publications,m.status," +
            "c.chapter_id, c.chapter_name,c.created_at) " +
            " FROM ReadingHistory rd JOIN User u ON rd.user = u.user_id JOIN Manga m ON rd.manga = m.manga_id JOIN Chapter c ON rd.chapter = c.chapter_id WHERE u.user_id =?1  ")
    List<UserReadingHistoryDTO> GetHistoriesByUserId(Long user_id);

    @Transactional
    @Modifying
    @Query("DELETE FROM ReadingHistory h WHERE h.user =:user")
    void deleteAllHistoryByUserId(@Param("user") User user);


    @Query("""
            SELECT his FROM ReadingHistory his
            WHERE his.user.user_id = ?1 AND his.manga.manga_id = ?2
            """)
    Optional<ReadingHistory> removeItem(Long userId, Long mangaId);

}
