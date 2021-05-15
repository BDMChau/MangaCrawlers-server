package serverapi.Tables.Manga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/manga")
public class MangaController {
    private final MangaService mangaService;

    @Autowired
    public MangaController(MangaService mangaService) {
        this.mangaService = mangaService;
    }


    @GetMapping("/getall")
    public ResponseEntity getAllManga(){

        return mangaService.getAllManga();
    }


    @PostMapping("/getbymanganame")
    public ResponseEntity getByMangaName(@RequestBody Map<String, Object> body){

        return mangaService.getByMangaName(body);
    }
}
