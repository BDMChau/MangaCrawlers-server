package serverapi.query.repository.forum;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.MangaGenreDTO;
import serverapi.tables.forum.post_category.PostCategory;

import java.util.List;


@Repository
public interface PostCategoryRepos extends JpaRepository<PostCategory, Long> {


    @Query("SELECT new serverapi.query.dtos.tables.MangaGenreDTO(g.genre_id, g.genre_name, g.genre_color, g.genre_description, m.manga_id) FROM " +
            "Manga m JOIN m.mangaGenres mg ON mg.manga = m.manga_id JOIN Genre g ON g.genre_id = mg.genre  ")
    List<MangaGenreDTO> getAll();
}
