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


    @PostMapping("/updateViewsChapter")
    public ResponseEntity updateViewsChapter(@RequestBody MangaPOJO mangaPOJO){
        System.out.println(mangaPOJO);
        System.out.println(mangaPOJO.getManga_id());

        return mangaService.updateViewsChapter(mangaPOJO);
    }

    @GetMapping("/getlastest")
    public ResponseEntity getLatest(){


        return mangaService.getLatest();
    }


}
