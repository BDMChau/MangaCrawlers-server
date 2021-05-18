package serverapi.Tables.Genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Queries.Repositories.GenreRepos;

import java.util.List;
import java.util.Map;

@Service
public class GenreService {
    private final GenreRepos genreRepos;

    @Autowired
    public GenreService(GenreRepos genreRepos) {
        this.genreRepos = genreRepos;
    }


    public ResponseEntity getAllgenres() {
        List<Genre> genres = genreRepos.findAll();

        if (genres.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "msg", "No genres!"
            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(),
                    HttpStatus.NO_CONTENT);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get manga page successfully!",
                "genres", genres
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

}
