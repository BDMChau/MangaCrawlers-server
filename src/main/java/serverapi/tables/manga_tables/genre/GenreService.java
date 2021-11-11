package serverapi.tables.manga_tables.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.query.repository.manga.GenreRepos;

import java.util.List;
import java.util.Map;

@Service
public class GenreService {
    private final GenreRepos genreRepos;

    @Autowired
    public GenreService(GenreRepos genreRepos) {
        this.genreRepos = genreRepos;
    }


    protected ResponseEntity getAllgenres() {
        List<Genre> genres = genreRepos.findAll();
        if (genres.isEmpty()) {
            Map<String, Object> err = Map.of("err", "No genres!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get all genres successfully!",
                "genres", genres
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

}
