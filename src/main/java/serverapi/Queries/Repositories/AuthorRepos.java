package serverapi.Queries.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.Queries.DTO.AuthorManga;
import serverapi.Tables.Author.Author;

import java.util.List;

@Repository
public interface AuthorRepos extends JpaRepository<Author, Long> {

    @Query(value = "SELECT * FROM Author", nativeQuery = true)
    List<Author> findAllAuthor();


    @Query(" SELECT new serverapi.Queries.DTO.AuthorManga(a.author_name, m.manga_name) FROM Author a JOIN a.mangas m ")
    List<AuthorManga> getnameAuthorManga();







}
