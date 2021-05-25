package serverapi.Tables.Manga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.Tables.Manga.POJO.MangaPOJO;

@RestController
@RequestMapping("/api/manga")
@CacheConfig(cacheNames={"manga"})
public class MangaController {
    private final MangaService mangaService;

    @Autowired
    public MangaController(MangaService mangaService) {
        this.mangaService = mangaService;
    }



    @PutMapping("/updateviewchapter")
    public ResponseEntity updateViewsChapter(@RequestBody MangaPOJO mangaPOJO) {
        Long mangaId = Long.parseLong(mangaPOJO.getManga_id());
        Long chapterId = Long.parseLong(mangaPOJO.getChapter_id());

        return mangaService.updateViewsChapter(mangaId, chapterId, mangaPOJO);
    }


    @Cacheable("latestMangas")
    @GetMapping("/getlastest")
    public ResponseEntity getLatest() {
        return mangaService.getLatest();
    }


    @Cacheable("topMangas")
    @GetMapping("/gettop")
    public ResponseEntity getTop() {
        return mangaService.getTop();
    }


    @PostMapping("/findmangafromgenre")
    public ResponseEntity findMangaFromGenre(@RequestBody MangaPOJO mangaPOJO) {
        Long genreId = Long.parseLong(mangaPOJO.getGenre_id());

        return mangaService.findMangaFromGenre(genreId);
    }

    @Cacheable("mangaPage")
    @GetMapping("/getmangapage")
    public ResponseEntity getMangaPage(@RequestParam(required = false) String manga_id) {

        return mangaService.getMangaPage(Long.parseLong(manga_id));
    }


    @GetMapping("/gettotalviews")
    public ResponseEntity getTotalView() {

        return mangaService.getTotalView();
    }


    @Cacheable("weeklyMangas")
    @GetMapping("/getweekly")
    public ResponseEntity getWeeklyMangas() {
        return mangaService.getWeeklyMangas();
    }


    @PostMapping("/searchmangas")
    public ResponseEntity searchMangasByName(@RequestBody MangaPOJO mangaPOJO) {
        String mangaName = mangaPOJO.getManga_name();

        return mangaService.searchMangasByName(mangaName);
    }


}
