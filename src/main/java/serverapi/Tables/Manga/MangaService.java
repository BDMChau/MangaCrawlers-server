package serverapi.Tables.Manga;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Queries.Repositories.MangaRepos;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MangaService {
    private final MangaRepos mangaRepository;

    @Autowired
    public MangaService(MangaRepos mangaRepository) {
        this.mangaRepository = mangaRepository;
    }

    public ResponseEntity getAllManga() {

        List<Manga> mangas = mangaRepository.findAll();

        Map<String, Object> msg = Map.of("msg", mangas);
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity getByMangaName(Map<String, Object> body) {
        System.out.println(body.get("name"));
        Optional<Manga> manga = mangaRepository.findByName((String) body.get("name"));

        if(!manga.isPresent()){
            Map<String, Object> err = Map.of("err", "This manga doesn't exist");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }

        Map<String, Object> msg = Map.of("msg", manga);
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
