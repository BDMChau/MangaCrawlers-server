package serverapi.query.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.FollowingDTO;
import serverapi.tables.user_tables.following_manga.FollowingManga;
import serverapi.tables.user_tables.user.User;

import javax.transaction.Transactional;
import java.util.List;


@Repository
public interface FollowingRepos extends JpaRepository<FollowingManga, Long> {

    @Query("SELECT new serverapi.query.dtos.tables.FollowingDTO(f.followingmanga_id, " +
            "m.manga_id, m.manga_name, m.status, m.description, m.stars, m.views, m.thumbnail, m.date_publications, m" +
            ".created_at) FROM " +
            "FollowingManga f JOIN f.user u ON u.user_id = f.user JOIN f.manga m ON f.manga = m.manga_id " +
            "WHERE u.user_id =?1")
    List<FollowingDTO> findByUserId(Long userId);

//    @Query("SELECT new serverapi.Query.DTO.ReportUserFollowMangaDTO(COUNT(u.user_id), m.manga_id, m.manga_name, m.thumbnail, m.stars, m.views, m.date_publications)"+
//            "FROM FollowingManga fm JOIN fm.manga m JOIN fm.user u GROUP BY m.manga_id, m.manga_name, m.thumbnail, m.stars, m.views, m.date_publications" +
//            " ORDER BY COUNT(u.user_id) DESC")
//    List<ReportUserFollowMangaDTO>findAllFollwingManga(Pageable pageable);






    @Transactional
    @Modifying
    @Query("DELETE FROM FollowingManga f WHERE f.user =:user")
    void deleteAllFollowByUserId(@Param("user") User user);


//   "SELECT new serverapi.Query.DTO.MangaChapterGenreDTO(c.chapter_id, c.chapter_name, c.created_at, m" +
//           ".manga_id,m.manga_name, m.thumbnail, g.genre_id, g.genre_name, g.genre_description, g.genre_color) FROM
//           " +
//           "Manga m JOIN m.chapters c JOIN m.mangaGenres mg ON mg.manga = m.manga_id JOIN Genre g ON g.genre_id =
//           mg.genre  " +
//           "WHERE c.chapter_id = (SELECT MAX(ct.chapter_id) FROM Manga mg INNER JOIN mg.chapters ct WHERE mg
//           .manga_id = m.manga_id " +
//           "AND g.genre_id =?1) Order by c.chapter_id Desc"
//   @Query("SELECT new serverapi.Query.DTO.FollowingDTO(f.followingmanga_id, " +
//           "m.manga_id, m.manga_name, m.status, m.description, m.stars, m.views, m.thumbnail, m.date_publications,
//           m.created_at) FROM " +
//           "FollowingManga f JOIN f.user u ON u.user_id = f.user JOIN f.manga m ON f.manga = m.manga_id " +
//           "WHERE u.user_id = ?1")
//   List<FollowingDTO> FindByUserId(Long UserId);

}
