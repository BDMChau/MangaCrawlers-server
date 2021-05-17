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


    @PostMapping("/getall")
    public ResponseEntity findChapters(@RequestBody Long manga_id, @RequestParam Long chapter_id){
        System.out.println(manga_id.getClass().getName());
        System.out.println(manga_id);


        return mangaService.findChapters(manga_id, chapter_id);
    }

//    @GetMapping("/getidauthor")
//    public ResponseEntity findAuthorId(){
//        return mangaService.findAuthorId();
//
//    }


    @PostMapping("/getbymanganame")
    public ResponseEntity getByMangaName(@RequestBody Map<String, Object> body){

        return mangaService.getByMangaName(body);
    }


    @GetMapping("/getlatestmanga")
    public ResponseEntity getLatestManga()
    {

        return mangaService.getLatestManga();


    }

    @GetMapping("/getallmanga")
    public ResponseEntity getallmanga(){
        return mangaService.getAllManga();

    }

    @GetMapping("/gettopmanga")
    public ResponseEntity getTopManga(){
     return   mangaService.getTopManga();
    }
}
