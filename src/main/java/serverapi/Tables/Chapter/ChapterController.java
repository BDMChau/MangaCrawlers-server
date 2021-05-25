package serverapi.Tables.Chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.Tables.Manga.POJO.MangaPOJO;

@RestController
@RequestMapping("/api/chapter")
@CacheConfig(cacheNames={"chapter"})
public class ChapterController {

    private final ChapterService chapterService;

    @Autowired
    public  ChapterController(ChapterService chapterService){
        this.chapterService = chapterService;

    }

    @GetMapping("/getall")
    public ResponseEntity getAllChapter(){


        return chapterService.getAllChapter();
    }


//    @GetMapping("/gettotalview")
//    public ResponseEntity getTotalView(){
//
//         return chapterService.getTotalView();
//
//    }


    @Cacheable(value = "topMangas", key = "#mangaPOJO.getChapter_id() + #mangaPOJO.getManga_id()")
    @PostMapping("/getimgchapter")
    public ResponseEntity findImgByChapter(@RequestBody MangaPOJO mangaPOJO){

        Long chapterId = Long.parseLong(mangaPOJO.getChapter_id());
        Long mangaId = Long.parseLong(mangaPOJO.getManga_id());

        return chapterService.findImgByChapter(chapterId, mangaId);
    }




}
