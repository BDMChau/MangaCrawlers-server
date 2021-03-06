package serverapi.tables.manga_tables.chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import serverapi.tables.manga_tables.manga.pojo.MangaPOJO;


@RestController
@RequestMapping("/api/chapter")
@CacheConfig(cacheNames = {"chapter"})
public class ChapterController {

    private final ChapterService chapterService;

    @Autowired
    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;

    }

    @GetMapping("/getall")
    public ResponseEntity getAllChapter() {


        return chapterService.getAllChapter();
    }

    @Cacheable(value = "total_chapters", key = "#mangaPOJO.getManga_id()")
    @GetMapping("/get_total_chapters")
    public ResponseEntity getTotalChapters(MangaPOJO mangaPOJO) {
        Long mangaId = Long.parseLong(mangaPOJO.getManga_id());

        return chapterService.getTotalChapters(mangaId);
    }

//    @GetMapping("/gettotalview")
//    public ResponseEntity getTotalView(){
//
//         return chapterService.getTotalView();
//
//    }

    @Transactional
    @Cacheable(value = "getimgschapter", key = "#mangaPOJO.getChapter_id() + #mangaPOJO.getManga_id()")
    @PostMapping("/getimgschapter")
    public ResponseEntity findImgByChapter(@RequestBody MangaPOJO mangaPOJO) {

        Long chapterId = Long.parseLong(mangaPOJO.getChapter_id());
        Long mangaId = Long.parseLong(mangaPOJO.getManga_id());

        return chapterService.findImgByChapter(chapterId, mangaId);
    }


}
