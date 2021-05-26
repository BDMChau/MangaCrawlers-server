package serverapi.Query.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;
import serverapi.Query.DTO.GenreDTO;
import serverapi.Tables.Genre.Genre;

import java.util.List;

@Repository
public interface GenreRepos extends JpaRepository<Genre, Long> {
    @Query("SELECT new serverapi.Query.DTO.GenreDTO(g.genre_id, g.genre_name, g.genre_color, g.genre_description) FROM " +
            "Manga m JOIN m.mangaGenres mg ON mg.manga = m.manga_id JOIN Genre g ON g.genre_id = mg.genre  " +
            "WHERE m.manga_id =?1")
    List<GenreDTO> findGenresByMangId(Long manga_id);

    @Async
    public <S extends Genre> S save(S entity);
}
