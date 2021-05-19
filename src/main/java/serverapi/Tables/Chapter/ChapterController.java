package serverapi.Tables.Chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chapter")
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





}
