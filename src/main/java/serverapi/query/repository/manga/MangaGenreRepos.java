package serverapi.query.repository.manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverapi.tables.manga_tables.manga_genre.MangaGenre;

@Repository
public interface MangaGenreRepos extends JpaRepository<MangaGenre, Long> {
}
