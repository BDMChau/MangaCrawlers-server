package serverapi.Tables.Manga;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Queries.DTO.MangaChapterDTO;
import serverapi.Queries.DTO.MangaChapterGenreDTO;
import serverapi.Queries.Repositories.ChapterRepos;
import serverapi.Queries.Repositories.MangaRepos;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.Manga.POJO.MangaPOJO;

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

        Optional<Manga> manga = mangaRepository.findById(Long.parseLong(mangaPOJO.getManga_id()));

        Manga manga1 = manga.get();

        manga1.getChapters().forEach(item -> {
            if (item.getChapter_id().equals(Long.parseLong(mangaPOJO.getChapter_id()))) {
                System.out.println(item.getViews());

                mangaPOJO.setChapter_name (item.getChapter_name ());
                int views = item.getViews();
                item.setViews(views + 1);
                chapterRepos.save(item);
            }


        });


        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas successfully!",
                "data", manga,
                "data2",manga1.getChapters ()
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
    public ResponseEntity findMangaFromGenre(MangaPOJO mangaPOJO) {

        List<MangaChapterGenreDTO> manga=mangaRepository.findMangaByOneGenre (Long.parseLong(mangaPOJO.getGenre_id ()));


        if(manga.isEmpty ()){
            Map<String, Object> msg = Map.of(
                    "msg", "Cannot get manga from this genre!",
                    "data", manga
            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas successfully!",
                "data", manga
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity getLatest() {
        List<MangaChapterDTO> latestChapterFromManga = mangaRepository.getLatestChapterFromManga();

        if (latestChapterFromManga.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "msg", "Nothing of latest mangas!"
            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get latest mangas successfully!",
                "data", latestChapterFromManga
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity getTop() {
        List<Manga> topfiveMangas = mangaRepository.getTop(10);

        if (topfiveMangas.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "msg", "Nothing of top six mangas!"
            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get top six mangas successfully!",
                "data", topfiveMangas
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity getMangaPage(Long mangaId) {
        Optional<Manga> manga = mangaRepository.findById(mangaId);

        if(!manga.isPresent()){
            Map<String, Object> err = Map.of(
                    "msg", "No content from manga page successfully!"
            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }

        List<Chapter> chapters = manga.get().getChapters();

        chapters.forEach(chapter ->{
            chapter.setManga(null);
        });




        Map<String, Object> msg = Map.of(
                "msg", "Get manga page successfully!",
                "manga", manga,
                "chapters", chapters
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }



}
