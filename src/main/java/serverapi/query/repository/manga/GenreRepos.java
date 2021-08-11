package serverapi.query.repository.manga;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.GenreDTO;
import serverapi.query.dtos.tables.MangaGenreDTO;
import serverapi.tables.manga_tables.genre.Genre;

import java.util.List;

@Repository
public interface GenreRepos extends JpaRepository<Genre, Long> {
    @Query("SELECT new serverapi.query.dtos.tables.GenreDTO(g.genre_id, g.genre_name, g.genre_color, g.genre_description) FROM " +
            "Manga m JOIN m.mangaGenres mg ON mg.manga = m.manga_id JOIN Genre g ON g.genre_id = mg.genre  " +
            "WHERE m.manga_id =?1")
    List<GenreDTO> findGenresByMangId(Long manga_id);

    @Query("SELECT new serverapi.query.dtos.tables.MangaGenreDTO(g.genre_id, g.genre_name, g.genre_color, g.genre_description, m.manga_id) FROM " +
            "Manga m JOIN m.mangaGenres mg ON mg.manga = m.manga_id JOIN Genre g ON g.genre_id = mg.genre  ")
    List<MangaGenreDTO> getAllGenresMangas();



}
