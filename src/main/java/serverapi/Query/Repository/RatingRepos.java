package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverapi.Tables.MangaTables.RatingManga.RatingManga;

@Repository
public interface RatingRepos extends JpaRepository<RatingManga, Long> {



}
