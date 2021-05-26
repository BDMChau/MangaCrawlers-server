package serverapi.Tables.Manga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Query.DTO.*;
import serverapi.Query.Repository.ChapterRepos;
import serverapi.Query.Repository.GenreRepos;
import serverapi.Query.Repository.MangaRepos;
import serverapi.Query.Repository.UpdateViewRepos;
import serverapi.Query.Specification.MangaSpecification;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.Manga.POJO.MangaPOJO;
import serverapi.Tables.UpdateView.UpdateView;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MangaService {
    private final MangaRepos mangaRepository;
    private final ChapterRepos chapterRepository;
    private final UpdateViewRepos updateViewRepos;
    private final GenreRepos genreRepository;

    @Autowired
    public MangaService(MangaRepos mangaRepository, ChapterRepos chapterRepository, UpdateViewRepos updateViewRepos, GenreRepos genreRepository) {
        this.mangaRepository = mangaRepository;
        this.chapterRepository = chapterRepository;
        this.updateViewRepos = updateViewRepos;
        this.genreRepository = genreRepository;
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

        List<Manga> listCurrentWeekly = mangaRepository.getWeekly(7,0);
        List<Manga> listPreviousWeekly = mangaRepository.getWeekly(14,7);
        System.out.println("con chó thịnh"+listCurrentWeekly);
        System.out.println("con đỉ thịnh:"+listPreviousWeekly);

        if(listCurrentWeekly.isEmpty() ){

            Map<String, Object> err = Map.of(
                    "msg", "Nothing from weekly mangas ranking!"
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, err).toJSON(), HttpStatus.OK);
        }

        if(listPreviousWeekly.isEmpty()){
            listPreviousWeekly = listCurrentWeekly;
            System.out.println("check previous"+listPreviousWeekly);
        }

        List<WeeklyMangaDTO> listWeeklyMangasRanking = new ArrayList<>();


        if(listCurrentWeekly.size() >= listPreviousWeekly.size()){
            System.out.println("câm"+listCurrentWeekly.size());
            System.out.println("cmcmc"+listPreviousWeekly.size());

            int temp = listCurrentWeekly.size() - listPreviousWeekly.size();
            int previousWeeklySize = listPreviousWeekly.size();
            for(int i =0; i<listCurrentWeekly.size();i++){

                for(int j=0 ; j<listPreviousWeekly.size() ; j++){

                    if(listCurrentWeekly.get(i).getManga_id().equals(listPreviousWeekly.get(j).getManga_id())){

                        WeeklyMangaDTO weeklyMangaDTO = new WeeklyMangaDTO();
                        System.out.println("listCurrentWeekly.get(i).getManga_id()"+listCurrentWeekly.get(i).getManga_id());

                       Long views = listCurrentWeekly.get(i).getViews() - listPreviousWeekly.get(j).getViews();

                       weeklyMangaDTO.setManga_id(listCurrentWeekly.get(i).getManga_id());
                       weeklyMangaDTO.setViews(listCurrentWeekly.get(i).getViews());
                       weeklyMangaDTO.setView_compares(views);
                        System.out.println("check set");

                       listWeeklyMangasRanking.add(j,weeklyMangaDTO);
                        System.out.println("listWeeklyMangasRanking.set(j,mangaDTO)"+listWeeklyMangasRanking);
                    }
                }

            }
            int z = 0;
            while (z < temp){

                WeeklyMangaDTO weeklyMangaDTO = new WeeklyMangaDTO();
                weeklyMangaDTO.setManga_id(listCurrentWeekly.get(previousWeeklySize + z).getManga_id());
                weeklyMangaDTO.setViews(listCurrentWeekly.get(previousWeeklySize + z).getViews());
                weeklyMangaDTO.setView_compares(listCurrentWeekly.get(previousWeeklySize + z).getViews());

                listWeeklyMangasRanking.add(weeklyMangaDTO);
                z++;
            }
        }
        else {

            int temp = listPreviousWeekly.size() - listCurrentWeekly.size();
            int previousWeeklySize = listPreviousWeekly.size();

            for(int i =0; i < listPreviousWeekly.size(); i++){

                for(int j=0; j < listCurrentWeekly.size(); j++){

                    if(listPreviousWeekly.get(i).getManga_id().equals(listCurrentWeekly.get(j).getManga_id())){

                        WeeklyMangaDTO weeklyMangaDTO = new WeeklyMangaDTO();
                        System.out.println("listPreviousWeekly.get(i).getManga_id()");

                        Long views = listCurrentWeekly.get(j).getViews() - listPreviousWeekly.get(i).getViews();

                        weeklyMangaDTO.setManga_id(listCurrentWeekly.get(j).getManga_id());
                        weeklyMangaDTO.setViews(listCurrentWeekly.get(j).getViews());
                        weeklyMangaDTO.setView_compares(views);

                        listWeeklyMangasRanking.add(weeklyMangaDTO);
                    }
                }

            }

        }

        System.out.println("hà phương"+listWeeklyMangasRanking);
        listWeeklyMangasRanking.sort(Comparator.comparing(WeeklyMangaDTO::getView_compares).reversed());
     //   listWeeklyMangasRanking.stream().limit(5L);
        System.out.println("list weekly manga ranking"+listWeeklyMangasRanking);

        List<Manga> listWeeklyRanking = new ArrayList<>();

        List<WeeklyMangaDTO> top5Mangas =  listWeeklyMangasRanking.stream().limit(5).collect(Collectors.toList());


        top5Mangas.forEach(item->{

            Optional<Manga> mangaOptional = mangaRepository.findById(item.getManga_id());
            Manga manga = mangaOptional.get();
            listWeeklyRanking.add(manga);

            System.out.println("totototo"+listWeeklyRanking);

        });

        if (listWeeklyRanking.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "msg", "Nothing from weekly mangas ranking empty!"
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, err).toJSON(), HttpStatus.OK);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get weekly mangas ranking successfully!",
                "data", listWeeklyRanking
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
}
