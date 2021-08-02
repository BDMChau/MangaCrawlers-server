package serverapi.Query.Repository.Manga;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTO.AuthorMangaDTO;
import serverapi.Tables.MangaTables.Author.Author;

import java.util.List;

@Repository
public interface AuthorRepos extends JpaRepository<Author, Long> {

    @Query(value = "SELECT * FROM Author", nativeQuery = true)
    List<Author> findAllAuthor();


    @Query(" SELECT new serverapi.Query.DTO.AuthorMangaDTO(a.author_name, m.manga_name) FROM Author a JOIN a.mangas" +
            " m ")
    List<AuthorMangaDTO> getNameAuthorManga();



    @Async
    public <S extends Author> S save(S entity);



}
