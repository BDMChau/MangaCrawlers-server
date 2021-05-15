package serverapi.Tables.Manga;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Queries.Repositories.MangaRepos;

import java.util.Map;
import java.util.Optional;

@Service
public class MangaService {
    private final MangaRepos mangaRepository;

    @Autowired
    public MangaService(MangaRepos mangaRepository) {
        this.mangaRepository = mangaRepository;
    }

    public ResponseEntity test(String name){

            Optional<Manga> manga = mangaRepository.findById(5L);
            System.out.println(manga.get().getChapters());
            System.out.println(manga.get().getManga_name());


        Map<String, Object> error = Map.of("msg", manga);

        return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
    }
}
