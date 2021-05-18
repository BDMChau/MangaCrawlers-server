package serverapi.Tables.Manga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.Tables.Manga.POJO.MangaPOJO;

@RestController
@RequestMapping("/api/manga")
public class MangaController {
    private final MangaService mangaService;

    @Autowired
    public MangaController(MangaService mangaService) {
        this.mangaService = mangaService;
    }



    @PutMapping("/updateviewchapter")
    public ResponseEntity updateViewsChapter(@RequestBody MangaPOJO mangaPOJO){

        return mangaService.updateViewsChapter(mangaPOJO);
    }

    @GetMapping("/getlastest")
    public ResponseEntity getLatest(){
        return mangaService.getLatest();
    }

    @GetMapping("/gettop")
    public ResponseEntity getTop(){
        return mangaService.getTop();
    }

    @PostMapping("/getmangapage")
    public ResponseEntity getMangaPage(@RequestBody MangaPOJO mangaPOJO){
        Long mangaId = Long.parseLong(mangaPOJO.getManga_id());

        return mangaService.getMangaPage(mangaId);
    }

    @GetMapping("/gettotalviews")
    public ResponseEntity getTotalView(){

        return mangaService.getTotalView();
    }

    @GetMapping("/getweeklytop")
    public ResponseEntity getWeeklyTop(){
        return mangaService.getWeeklyTop();
    }



}
