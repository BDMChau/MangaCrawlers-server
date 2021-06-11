package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTO.AverageStarDTO;
import serverapi.Query.DTO.RatingMangaDTO;
import serverapi.Tables.RatingManga.RatingManga;

import java.util.List;

@Repository
public interface RatingMangaRepos extends JpaRepository<RatingManga, Long> {

    @Query("SELECT new serverapi.Query.DTO.RatingMangaDTO(rm.ratingmanga_id, u.user_id, " +
            "u.user_name, u.user_email," +
            "m .manga_id, m.manga_name, m.thumbnail, m.stars, m.views, m.date_publications, m. status) " +
            "FROM RatingManga rm JOIN Manga m ON rm.manga = m.manga_id JOIN User u ON rm.user = u.user_id WHERE u" +
            ".user_id =?1")
    List<RatingMangaDTO> ratingManga(Long userId);

    @Query("SELECT new serverapi.Query.DTO.AverageStarDTO(AVG(rm.value), m.manga_id) FROM RatingManga rm JOIN rm" +
            ".manga m GROUP BY m.manga_id ")
    List<AverageStarDTO> avgRatingManga();


    @Query(value = "SELECT rm from RatingManga rm JOIN Manga m ON rm.manga = m.manga_id where m.manga_id = ?1")
    List<RatingManga> findAllByMangaId(Long mangaId);


}
