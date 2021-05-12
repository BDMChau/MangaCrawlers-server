package serverapi.Tables.Manga;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;

import java.util.Map;
import java.util.Optional;

@Service
public class MangaService {
    private final MangaRepository mangaRepository;

    @Autowired
    public MangaService(MangaRepository mangaRepository) {
        this.mangaRepository = mangaRepository;
    }

    public ResponseEntity test(String name){

        try{
            Optional<Manga> manga = mangaRepository.findByName("Black Clover");
            System.out.println(manga);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        Map<String, String> error = Map.of("msg", "AAAAAAAAAAAAAA!");

        return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, error).toJSON(), HttpStatus.ACCEPTED);
    }
}
