package serverapi.Queries.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import serverapi.Tables.Author.Author;

public interface AuthorRepos extends JpaRepository<Author, Long> {
}
