package serverapi.Tables.Manga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Queries.DTO.MangaChapterDTO;
import serverapi.Queries.DTO.MangaChapterGenreDTO;
import serverapi.Queries.DTO.MangaViewDTO;
import serverapi.Queries.DTO.SearchCriteriaDTO;
import serverapi.Queries.Repositories.ChapterRepos;
import serverapi.Queries.Repositories.MangaRepos;
import serverapi.Queries.Repositories.UpdateViewRepos;
import serverapi.Queries.Specification.MangaSpecification;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.Manga.POJO.MangaPOJO;
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

                chapterRepos.save(item);
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


        Map<String, Object> msg = Map.of("msg", "Get latest mangas successfully!", "data", latestChapterFromManga);
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
        Optional<Manga> manga = mangaRepository.findById(mangaId);

        if (manga.isEmpty()) {
            Map<String, Object> err = Map.of("msg", "No content from manga page!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }
        manga.get().getAuthor().getAuthor_name();

        List<Object> genres = new ArrayList<>();
        manga.get().getMangaGenres().forEach(genre -> {
            Long genreId = genre.getGenre().getGenre_id();
            String genreName = genre.getGenre().getGenre_name();
            String genreColor = genre.getGenre().getGenre_color();
            String genreDesc = genre.getGenre().getGenre_description();

            Map<String, Object> genreObj = Map.of(
                    "genre_id", genreId,
                    "genre_name", genreName,
                    "genre_desc", genreDesc,
                    "genre_color", genreColor
            );
            genres.add(genreObj);
        });


        List<Chapter> chapters = manga.get().getChapters();
        chapters.forEach(chapter -> {
            chapter.setManga(null);
        });

        Map<String, Object> msg = Map.of("msg", "Get manga page successfully!",
                "manga", manga,
                "chapters", chapters,
                "genres", genres
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

        listViewsMangas.forEach(item -> {
            Long mangaId = item.getManga_id();
            Long totalViews = item.getViews();
            Calendar createdAt = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

            Manga manga = new Manga();
            manga.setManga_id(mangaId);
            if (totalViews.equals(0L)) {
                manga.setViews(0L);
            } else {
                manga.setViews(totalViews);
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

        if(searchingResults.isEmpty()){
            Map<String, Object> err = Map.of(
                    "err", "No manga!"
            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get weekly mangas ranking successfully!",
                "data", searchingResults
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
