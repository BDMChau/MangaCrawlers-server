package serverapi.Query.Repository.Manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverapi.Tables.MangaTables.MangaGenre.MangaGenre;

@Repository
public interface MangaGenreRepos extends JpaRepository<MangaGenre, Long> {
}
