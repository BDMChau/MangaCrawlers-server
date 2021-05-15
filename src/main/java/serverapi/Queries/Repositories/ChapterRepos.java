package serverapi.Queries.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import serverapi.Tables.Chapter.Chapter;

public interface ChapterRepos extends JpaRepository<Chapter, Long> {
}
