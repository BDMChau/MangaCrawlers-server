package serverapi.Tables.Manga;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Queries.Repositories.ChapterRepos;
import serverapi.Queries.Repositories.MangaRepos;
import serverapi.Tables.Manga.POJO.MangaPOJO;
import serverapi.Queries.DTO.LatestManga;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MangaService {
    private final MangaRepos mangaRepository;
    private final ChapterRepos chapterRepos;

    @Autowired
    public MangaService(MangaRepos mangaRepository, ChapterRepos chapterRepos) {
        this.mangaRepository = mangaRepository;
        this.chapterRepos = chapterRepos;
    }

    public ResponseEntity updateViewsChapter(MangaPOJO mangaPOJO) {

        Optional<Manga> manga=mangaRepository.findById(Long.parseLong(mangaPOJO.getManga_id()));

        Manga manga1 = manga.get();

        manga1.getChapters().forEach(item ->{
            if(item.getChapter_id().equals(Long.parseLong(mangaPOJO.getChapter_id()))){
                System.out.println(item.getViews());
                int views = item.getViews();
                item.setViews(views+1);
                chapterRepos.save(item);
            }


        });


        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas successfully!",
                "data", manga
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity getLatest() {
        List<LatestManga> latestChapterFromManga = mangaRepository.getLatestChapterFromManga();

//        List chapter = chapterRepos.findById();
//
//        mangas.forEach(manga->{
//            List<Chapter> chapters
//        });



        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas successfully!",
                "data", latestChapterFromManga
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
