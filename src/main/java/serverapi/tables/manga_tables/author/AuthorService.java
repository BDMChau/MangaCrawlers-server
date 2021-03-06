package serverapi.tables.manga_tables.author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.query.dtos.tables.AuthorMangaDTO;
import serverapi.query.repository.manga.AuthorRepos;

import java.util.List;
import java.util.Map;

@Service
public class AuthorService {

    private final AuthorRepos authorRepos;

    @Autowired
    public AuthorService(AuthorRepos authorRepos) {
        this.authorRepos = authorRepos;
    }

    public ResponseEntity finaAllAuthor() {
        List<Author> authors = authorRepos.findAllAuthor();

        Map<String, Object> msg = Map.of(
                "msg", "Get all authors successfully!",
                "data", authors
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity getnameAuthorManga() {
        List<AuthorMangaDTO> authors = authorRepos.getNameAuthorManga();
//         Author author = authors.get();
//         List<Manga> mangaList = (List<Manga>) author.getMangas();

        Map<String, Object> msg = Map.of(
                "msg", "Get all authors, mangas successfully!",
                "data", authors

        );

        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
