package serverapi.Queries.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import serverapi.Tables.Author.Author;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.dto.AuthorManga;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepos extends JpaRepository<Author, Long> {

    @Query(value = "SELECT * FROM Author", nativeQuery = true)
    List<Author> findAllAuthor();


    @Query(" SELECT new serverapi.Tables.dto.AuthorManga(a.author_name, m.manga_name) FROM Author a JOIN a.mangas m ")
    List<AuthorManga> getnameAuthorManga();




}
