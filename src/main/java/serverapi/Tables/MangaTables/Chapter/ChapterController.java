package serverapi.Tables.MangaTables.Chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import serverapi.Tables.Manga.POJO.CommentPOJO;
import serverapi.Tables.Manga.POJO.MangaPOJO;

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


    @Transactional
    @PostMapping("/getcommentschapter")
    public ResponseEntity getCommentsChapter(@RequestBody CommentPOJO commentPOJO) {

        Long chapterId = Long.parseLong(commentPOJO.getChapter_id ());
        System.out.println ("chapterId"+chapterId);

        int from = commentPOJO.getFrom ();
        System.out.println ("from_"+from);

        int amount = commentPOJO.getAmount();
        System.out.println("amount_" + amount);

        Long mangaId = Long.parseLong (commentPOJO.getManga_id ());

        return chapterService.getCommentsChapter(mangaId ,chapterId, amount, from);
    }


}
