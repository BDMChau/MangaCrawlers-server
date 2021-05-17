package serverapi.Tables.Chapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Queries.Repositories.ChapterRepos;

import java.util.List;
import java.util.Map;

@Service
public class ChapterService {

    private final ChapterRepos chapterRepos;

    @Autowired
    public ChapterService(ChapterRepos chapterRepos) {
        this.chapterRepos = chapterRepos;
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
}
