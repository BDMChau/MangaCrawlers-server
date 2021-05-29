package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTO.RatingMangaDTO;
import serverapi.Tables.RatingManga.RatingManga;

import java.util.Optional;

@Repository
public interface RatingRepos extends JpaRepository<RatingManga, Long> {



}
