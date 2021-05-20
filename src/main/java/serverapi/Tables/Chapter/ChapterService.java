package serverapi.Tables.Chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Query.DTO.ChapterDTO;
import serverapi.Query.DTO.ChapterImgDTO;
import serverapi.Query.Repository.ChapterRepos;
import serverapi.Query.Repository.ImgChapterRepos;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ChapterService {

    private final ChapterRepos chapterRepos;
    private final ImgChapterRepos imgChapterRepos;

    @Autowired
    public ChapterService(ChapterRepos chapterRepos, ImgChapterRepos imgChapterRepos) {
        this.chapterRepos = chapterRepos;
        this.imgChapterRepos = imgChapterRepos;
    }


    public ResponseEntity getAllChapter(){
        List<Chapter> chapters = chapterRepos.findAllChapter();

        Map<String, Object> msg = Map.of(
                "msg", "Get all chapters successfully!",
                "data", chapters
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


//    public ResponseEntity getlatestmanga(){
//        List<LatestManga> latestMangas = ();
//
//
//        Map<String, Object> msg = Map.of(
//                "msg", "Get all mangas, chapters successfully!",
//                "data", latestMangas
//
//
//        );
//        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
//    }

//    public ResponseEntity findmaxid(){
//        List<Chapter> chapters = chapterRepos.findmaxid();
//
//
//
//        Map<String, Object> msg = Map.of(
//                "msg", "Get all chapters successfully!",
//                "data", chapters
//        );
//        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
//    }


//    public ResponseEntity getTotalView(){
//
//        List<Chapter> chapters = chapterRepos.getTotalView();
//
//        Map<String, Object> msg = Map.of(
//                "msg", "Get all total views chapters successfully!",
//                "data", chapters
//        );
//        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
//    }
    public ResponseEntity findImgByChapter(Long chapterId, Long mangaId){
        Optional<Chapter> chapter = chapterRepos.findById(chapterId);

        List<ChapterDTO> chapterDTOS = chapterRepos.findChaptersbyMangaId(mangaId);

        List<ChapterImgDTO> chapterImgDTOS = imgChapterRepos.findImgsByChapterId(chapterId);

        Map<String, Object> msg = Map.of(
                "msg", "Get all chapters successfully!",
                "chapterInfo",chapter,
                "listChapter", chapterDTOS,
                "listImg", chapterImgDTOS


        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }
}
