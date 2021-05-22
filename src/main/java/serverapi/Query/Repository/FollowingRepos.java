package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTO.FollowingDTO;
import serverapi.Tables.FollowingManga.FollowingManga;
import java.util.List;


@Repository
public interface FollowingRepos extends JpaRepository<FollowingManga, Long> {

   @Query("SELECT new serverapi.Query.DTO.FollowingDTO(f.followingmanga_id, " +
           "m.manga_id, m.manga_name, m.status, m.description, m.stars, m.views, m.thumbnail, m.date_publications, m.createdAt) FROM " +
           "FollowingManga f JOIN f.user u ON u.user_id = f.user JOIN f.manga m ON f.manga = m.manga_id "+
           "WHERE u.user_id =?1")
   List<FollowingDTO> FindByUserId(Long UserId);


//   "SELECT new serverapi.Query.DTO.MangaChapterGenreDTO(c.chapter_id, c.chapter_name, c.createdAt, m" +
//           ".manga_id,m.manga_name, m.thumbnail, g.genre_id, g.genre_name, g.genre_description, g.genre_color) FROM " +
//           "Manga m JOIN m.chapters c JOIN m.mangaGenres mg ON mg.manga = m.manga_id JOIN Genre g ON g.genre_id = mg.genre  " +
//           "WHERE c.chapter_id = (SELECT MAX(ct.chapter_id) FROM Manga mg INNER JOIN mg.chapters ct WHERE mg.manga_id = m.manga_id " +
//           "AND g.genre_id =?1) Order by c.chapter_id Desc"
//   @Query("SELECT new serverapi.Query.DTO.FollowingDTO(f.followingmanga_id, " +
//           "m.manga_id, m.manga_name, m.status, m.description, m.stars, m.views, m.thumbnail, m.date_publications, m.createdAt) FROM " +
//           "FollowingManga f JOIN f.user u ON u.user_id = f.user JOIN f.manga m ON f.manga = m.manga_id " +
//           "WHERE u.user_id = ?1")
//   List<FollowingDTO> FindByUserId(Long UserId);

}
