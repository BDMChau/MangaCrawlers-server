package serverapi.Queries.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import serverapi.Tables.Genre.Genre;

@Repository
public interface GenreRepos extends JpaRepository<Genre, Long> {
}
