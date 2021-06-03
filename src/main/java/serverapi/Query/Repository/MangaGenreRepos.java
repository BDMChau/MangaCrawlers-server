package serverapi.Query.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverapi.Tables.MangaGenre.MangaGenre;

@Repository
public interface MangaGenreRepos extends JpaRepository<MangaGenre, Long> {
}
