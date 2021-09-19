package serverapi.tables.manga_tables.manga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.tables.manga_tables.manga.pojo.CommentPOJO;
import serverapi.tables.manga_tables.manga.pojo.MangaPOJO;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/manga")
@CacheConfig(cacheNames = {"manga"})
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


    @Cacheable(value = "latestMangas", key = "#root.method")
    @GetMapping("/getlastest")
    public ResponseEntity getLatest() {
        return mangaService.getLatest();
    }


    @Cacheable(value = "topMangas", key = "#root.method")
    @GetMapping("/gettop")
    public ResponseEntity getTop() {
        return mangaService.getTop();
    }


    @Cacheable(value = "findmangafromgenre", key = "#mangaPOJO.getGenre_id()")
    @PostMapping("/findmangafromgenre")
    public ResponseEntity findMangaFromGenre(@RequestBody MangaPOJO mangaPOJO) {
        Long genreId = Long.parseLong(mangaPOJO.getGenre_id());

        return mangaService.findMangaFromGenre(genreId);
    }


    @Cacheable(value = "mangaPage", key = "#manga_id")
    @GetMapping("/getmangapage")
    public ResponseEntity getMangaPage(@RequestParam(required = false) String manga_id) {

        return mangaService.getMangaPage(Long.parseLong(manga_id));
    }


    @GetMapping("/gettotalviews")
    public ResponseEntity getTotalView() {

        return mangaService.getTotalView();
    }


    @Cacheable(value = "weeklyMangas", key = "#root.method")
    @GetMapping("/getweekly")
    public ResponseEntity getWeeklyMangas() {
        return mangaService.getWeeklyMangas();
    }


    @Cacheable(value = "dailyMangas", key = "#root.method")
    @GetMapping("/getdaily")
    public ResponseEntity getDailyMangas() {
        return mangaService.getDailyMangas();
    }


    // random
    @GetMapping("/getsuggestion")
    public ResponseEntity getSuggestionList() {

        return mangaService.getSuggestionList();
    }


    @PostMapping("/searchmangas")
    public ResponseEntity searchMangasByName(@RequestBody MangaPOJO mangaPOJO) {
        String mangaName = mangaPOJO.getManga_name();

        return mangaService.searchMangasByName(mangaName);
    }


    @PostMapping("/advancedsearch")
    public ResponseEntity searchMangasByGenres(@RequestBody Map data) {

        List<Integer> listIntGenId = (List<Integer>) data.get("genres_id");
        List<Long> listGenId = new ArrayList<>();

        for (Integer genreId : listIntGenId) {
            Long id = Long.parseLong(genreId.toString());
            listGenId.add(id);
        }


        return mangaService.searchMangasByGenres(listGenId);
    }


    @PostMapping("/getcommentsmanga")
    public ResponseEntity getCommentsManga(@RequestBody CommentPOJO commentPOJO) {

        Long mangaId = Long.parseLong(commentPOJO.getManga_id());

        int from = commentPOJO.getFrom();
        System.out.println("from_" + from);

        int amount = commentPOJO.getAmount();
        System.out.println("amount_" + amount);

        return mangaService.getCommentsManga(mangaId, from, amount);
    }


}
