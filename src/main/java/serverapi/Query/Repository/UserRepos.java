package serverapi.Query.Repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTO.*;
import serverapi.Tables.User.User;

import java.util.Calendar;
import java.util.List;

@Repository
public interface UserRepos extends JpaRepository<User, Long> {

    @Query("SELECT new serverapi.Query.DTO.ChapterDTO(c.chapter_id, c.chapter_name, c.createdAt) FROM Chapter c JOIN c.readingHistories rd WHERE c.chapter_id =?1 ")
    List<ChapterDTO> findChapterByReadingHistory(Long chapter_id);

    @Query("SELECT new serverapi.Query.DTO.MangaDTO(m.manga_id, m.manga_name, m.thumbnail) FROM Manga m JOIN m.readingHistories rd WHERE m.manga_id =?1 ")
    List<MangaDTO> findMangaByReadingHistory(Long manga_id);

    @Query("SELECT new serverapi.Query.DTO.ReportUserFollowMangaDTO(COUNT(u.user_id), a.author_id, a.author_name, m.manga_id, m.manga_name, m.thumbnail, m.stars, m.views, m.date_publications)"+
            "FROM FollowingManga fm JOIN fm.manga m JOIN fm.user u JOIN m.author a GROUP BY a.author_id, a.author_name, m.manga_id, m.manga_name, m.thumbnail, m.stars, m.views, m.date_publications" +
            " ORDER BY COUNT(u.user_id) DESC")
    List<ReportUserFollowMangaDTO>findAllFollwingManga(Pageable pageable);

    @Query("SELECT new serverapi.Query.DTO.ReportTopMangaDTO(a.author_id,a.author_name, m.manga_id, m.manga_name, m.thumbnail, m.stars, m.views, m.date_publications, m.createdAt)" +
            "FROM Manga m JOIN m.author a ORDER BY m.views DESC ")
    List<ReportTopMangaDTO> findTopManga(Pageable pageable);

//    @Query("SELECT new serverapi.Query.DTO.ReportUserDTO(COUNT(u.user_id),u.user_name, u.user_email, u.user_avatar, u.user_isVerified, u.createdAt) FROM User u GROUP BY u.user_name, u.user_email,u.user_avatar,u.user_isVerified ")
//    List<ReportUserDTO> getUser();

    @Query("SELECT new serverapi.Query.DTO.UserDTO(u.user_id, u.createdAt) FROM User u")
    List<UserDTO> getAllUser();


}

