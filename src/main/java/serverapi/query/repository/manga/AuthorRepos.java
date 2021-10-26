package serverapi.query.repository.manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.AuthorMangaDTO;
import serverapi.tables.manga_tables.author.Author;

import java.util.List;

@Repository
public interface AuthorRepos extends JpaRepository<Author, Long> {

    @Query(value = "SELECT * FROM Author", nativeQuery = true)
    List<Author> findAllAuthor();


    @Query(" SELECT new serverapi.query.dtos.tables.AuthorMangaDTO(a.author_name, m.manga_name) FROM Author a JOIN a.mangas" +
            " m ")
    List<AuthorMangaDTO> getNameAuthorManga();



    @Async
    <S extends Author> S save(S entity);



}
