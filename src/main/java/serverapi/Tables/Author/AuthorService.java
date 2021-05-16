package serverapi.Tables.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Queries.Repositories.AuthorRepos;
import serverapi.Queries.Repositories.MangaRepos;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.dto.AuthorManga;

import java.lang.management.OperatingSystemMXBean;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthorService {

    private AuthorRepos authorRepos;

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
        List<AuthorManga> authors = authorRepos.getnameAuthorManga();
//         Author author = authors.get();
//         List<Manga> mangaList = (List<Manga>) author.getMangas();

        Map<String, Object> msg = Map.of(
                "msg", "Get all authors, mangas successfully!",
                "data", authors

        );

        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
