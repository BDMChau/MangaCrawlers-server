package serverapi.Tables.Manga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Helpers.OffsetBasedPageRequest;
import serverapi.Query.DTO.*;
import serverapi.Query.Repository.*;
import serverapi.Query.Specification.MangaSpecification;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.Manga.POJO.MangaPOJO;
import serverapi.Tables.UpdateView.UpdateView;

import java.util.*;

@Service
public class MangaService {
    private final MangaRepos mangaRepository;
    private final ChapterRepos chapterRepository;
    private final UpdateViewRepos updateViewRepos;
    private final GenreRepos genreRepository;
    private final ChapterCommentsRepos chapterCommentsRepos;

    @Autowired
    public MangaService(MangaRepos mangaRepository, ChapterRepos chapterRepository, UpdateViewRepos updateViewRepos, GenreRepos genreRepository, ChapterCommentsRepos chapterCommentsRepos) {
        this.mangaRepository = mangaRepository;
        this.chapterRepository = chapterRepository;
        this.updateViewRepos = updateViewRepos;
        this.genreRepository = genreRepository;
        this.chapterCommentsRepos = chapterCommentsRepos;
    }

    public ResponseEntity updateViewsChapter(Long mangaId, Long chapterId, MangaPOJO mangaPOJO) {

        Optional<Manga> manga = mangaRepository.findById(mangaId);

        if (manga.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "No mangas!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }

        List<Chapter> chapters = manga.get().getChapters();

        chapters.forEach(item -> {
            if (item.getChapter_id().equals(chapterId)) {
                String chapterName = item.getChapter_name();
                mangaPOJO.setChapter_name(chapterName);

                int views = item.getViews();
                item.setViews(views + 1);

                chapterRepository.save(item);
            }
        });

        Map<String, Object> msg = Map.of(
                "msg", "Get all mangas successfully!",
                "data", manga,
                "data2", chapters
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity findMangaFromGenre(Long genreId) {

        List<MangaChapterGenreDTO> manga = mangaRepository.findMangaByOneGenre(genreId);

        if (manga.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Cannot get manga from this genre!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of("msg", "Get mangas from this genre successfully!", "data", manga);
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity getLatest() {
        List<MangaChapterDTO> latestChapterFromManga = mangaRepository.getLatestChapterFromManga();

        if (latestChapterFromManga.isEmpty()) {
            Map<String, Object> err = Map.of("msg", "Nothing of latest mangas!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get latest mangas successfully!",
                "data", latestChapterFromManga
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity getTop() {
        List<Manga> topfiveMangas = mangaRepository.getTop(5);

        if (topfiveMangas.isEmpty()) {
            Map<String, Object> err = Map.of("msg", "Nothing of top mangas!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of("msg", "Get top five mangas successfully!", "data", topfiveMangas);
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity getMangaPage(Long mangaId) {
        Optional<AuthorMangaDTO> manga = mangaRepository.getAllByMangaId(mangaId);
        List<GenreDTO> genres = genreRepository.findGenresByMangId(mangaId);
        List<ChapterDTO> chapters = chapterRepository.findChaptersbyMangaId(mangaId);

        if (manga.isEmpty()) {
            Map<String, Object> err = Map.of("msg", "No content from manga page!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get manga page successfully!",
                "manga", manga,
                "genres", genres,
                "chapters", chapters
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    //    set interval task
    public ResponseEntity getTotalView() {
        List<MangaViewDTO> listViewsMangas = mangaRepository.getTotalView();

        if (listViewsMangas.isEmpty()) {
            Map<String, Object> err = Map.of("msg", "Nothing from total views mangas successfully!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }
//
        listViewsMangas.forEach(item -> {
            Long mangaId = item.getManga_id();
            Long totalViews = item.getViews();
            Calendar createdAt = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

            Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);

            Manga manga = mangaOptional.get();

            if (totalViews.equals(0L)) {
                manga.setViews(0L);
                mangaRepository.save(manga);
            } else {
                manga.setViews(totalViews);
                mangaRepository.saveAndFlush(manga);
            }

            UpdateView view = new UpdateView();
            view.setTotalviews(totalViews);
            view.setCreatedAt(createdAt);
            view.setManga(manga);

            updateViewRepos.save(view);
        });

        Map<String, Object> msg = Map.of(
                "msg", "Get total views mangas successfully!",
                "data", listViewsMangas
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity getWeeklyMangas() {
        List<Manga> listWeeklyMangasRanking = mangaRepository.getWeekly(PageRequest.of(0, 5));

        if (listWeeklyMangasRanking.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "msg", "Nothing from weekly mangas ranking!"
            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get weekly mangas ranking successfully!",
                "data", listWeeklyMangasRanking
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity searchMangasByName(String mangaName) {
        MangaSpecification specific =
                new MangaSpecification(new SearchCriteriaDTO("manga_name", ":", mangaName));

        List<Manga> searchingResults = mangaRepository.findAll(specific);

        if (searchingResults.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "No manga!",
                    "data", searchingResults
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get search results successfully!",
                "data", searchingResults
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity getCommentsManga(Long mangaId, int amount, int from) {

        //get list comments in 1 chapter
        Optional<AuthorMangaDTO> mangaOptional = mangaRepository.getAllByMangaId (mangaId);

        if (mangaOptional.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "Empty mangas!"
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }
        List<ChapterDTO> chapterDTOList = chapterRepository.findChaptersbyMangaId (mangaId);
        System.out.println ("chapterDTOList "+chapterDTOList);

        chapterDTOList.forEach (items ->{
            Pageable pageableChapter = new OffsetBasedPageRequest (0, 1);
            List<ChapterCommentsDTO> chapterCommentsDTOList = chapterCommentsRepos.getCommentsChapter (items.getChapter_id (), pageableChapter);
        });

//
//        if (chapterCommentsDTOList.isEmpty()) {
//            Map<String, Object> msg = Map.of("msg", "No comment found!");
//            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
//        }


        Map<String, Object> msg = Map.of(
                "msg", "Get chapter comment successfully!"
//                "Info", chapterCommentsDTOList
//
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
