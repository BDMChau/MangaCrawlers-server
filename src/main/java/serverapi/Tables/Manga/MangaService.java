package serverapi.Tables.Manga;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Queries.DTO.TotalViews;
import serverapi.Queries.Repositories.ChapterRepos;
import serverapi.Queries.Repositories.MangaRepos;
import serverapi.Queries.Repositories.UpdateViewRepos;
import serverapi.Tables.Manga.POJO.MangaPOJO;
import serverapi.Queries.DTO.MangaChapterDTO;
import serverapi.Tables.UpdateView.UpdateView;

import java.util.*;

@Service
public class MangaService {
    private final MangaRepos mangaRepository;
    private final ChapterRepos chapterRepos;
    private final UpdateViewRepos updateViewRepos;

    @Autowired
    public MangaService(MangaRepos mangaRepository, ChapterRepos chapterRepos, UpdateViewRepos updateViewRepos) {
        this.mangaRepository = mangaRepository;
        this.chapterRepos = chapterRepos;
        this.updateViewRepos = updateViewRepos;
    }

    public ResponseEntity updateViewsChapter(MangaPOJO mangaPOJO) {

        Optional<Manga> manga=mangaRepository.findById(Long.parseLong(mangaPOJO.getManga_id()));

        Manga manga1 = manga.get();

        manga1.getChapters().forEach(item ->{
            if(item.getChapter_id().equals(Long.parseLong(mangaPOJO.getChapter_id()))){
                System.out.println(item.getViews());
                int views = item.getViews();
                item.setViews(views+1);

            }


        });
        mangaRepository.save(manga1);


        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas successfully!",
                "data", manga
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity getLatest() {
        List<MangaChapterDTO> latestChapterFromManga = mangaRepository.getLatestChapterFromManga();

        if(latestChapterFromManga.isEmpty()){
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


    public ResponseEntity getTop(){
        List<Manga> topfiveMangas = mangaRepository.getTop(10);

        if(topfiveMangas.isEmpty()){
            Map<String, Object> err = Map.of(
                    "msg", "Nothing of top five mangas!"
            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get top five mangas successfully!",
                "data", topfiveMangas
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity getMangaPage(Long mangaId){
        Optional<Manga> manga = mangaRepository.findById(mangaId);

        if(!manga.isPresent()){
            Map<String, Object> err = Map.of(
                    "err", "Nothing manga!"
            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get top five mangas successfully!",
                "data", manga
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity getTotalView(){

        List<TotalViews> totalViewsManga = mangaRepository.getTotalView();

        List<UpdateView> updatedViewList = new ArrayList<>();

        totalViewsManga.forEach(item->{
            Long mangaId = item.getManga_id();
            Long totalViews = item.getViews();
            Calendar createdAt = Time

            Map<String, Object> element = Map.of(
                    "manga_id", mangaId,
                    "total_views", totalViews,
                    "created_at", totalViews
            );

            updatedViewList.add(element);

        });


        Map<String, Object> msg = Map.of(
                "msg", "Get total views manga successfully!",
                "data", totalViewsManga
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


}
