package serverapi.Tables.Manga;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Queries.Repositories.MangaRepos;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.dto.LatestManga;
import serverapi.Tables.dto.SumViewChapter;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MangaService {
    private final MangaRepos mangaRepository;

    @Autowired
    public MangaService(MangaRepos mangaRepository) {
        this.mangaRepository = mangaRepository;
    }

    public ResponseEntity findMangabyId() {

        Optional<Manga> mangas = mangaRepository.findById(5L);
        Manga mangalist = mangas.get();
        List<Chapter> chapterList = (List<Chapter>) mangalist.getChapters();



        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas successfully!",
                "data", mangas,
                "data1", chapterList

        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity getByMangaName(Map<String, Object> body) {
        System.out.println(body.get("name"));
        Optional<Manga> manga = mangaRepository.findByName((String) body.get("name"));

        if(!manga.isPresent()){
            Map<String, Object> err = Map.of("err", "This manga doesn't exist");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }

        Map<String, Object> msg = Map.of("msg", manga);
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

//
//    public ResponseEntity findAuthorId(){
//        List<Manga> mangas = mangaRepository.findAuthorId();
//
//        Map<String, Object> msg = Map.of(
//                "msg", "Get all mangas successfully!",
//                "data", mangas
//
//
//        );
//       return new  ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
//    }


    public ResponseEntity getLatestManga(){
        List<LatestManga> latestMangas = mangaRepository.getLatestManga();


        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas, chapters successfully!",
                "data", latestMangas


        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity getAllManga(){
        List<Manga> getallmanga = mangaRepository.getAllManga();

        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas successfully!",
                "data", getallmanga


        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity getTopManga(){
        List<Manga> gettopmanga = mangaRepository.getTopManga();

        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas successfully!",
                "data", gettopmanga


        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

}
