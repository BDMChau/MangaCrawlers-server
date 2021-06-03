package serverapi.Tables.Manga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Helpers.AdvancedSearchGenreId;
import serverapi.Helpers.OffsetBasedPageRequest;
import serverapi.Query.DTO.*;
import serverapi.Query.Repository.*;
import serverapi.Query.Specification.MangaSpecification;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.Genre.Genre;
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
    private final ChapterCommentsRepos chapterCommentsRepos;

    @Autowired
    public MangaService(MangaRepos mangaRepository, ChapterRepos chapterRepository, UpdateViewRepos updateViewRepos,
                        GenreRepos genreRepository, ChapterCommentsRepos chapterCommentsRepos) {
        this.mangaRepository = mangaRepository;
        this.chapterRepository = chapterRepository;
        this.updateViewRepos = updateViewRepos;
        this.genreRepository = genreRepository;
        this.chapterCommentsRepos = chapterCommentsRepos;
    }

    public ResponseEntity updateViewsChapter(Long mangaId, Long chapterId, MangaPOJO mangaPOJO) {

        Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);

        if (mangaOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "No mangas!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }

        List<Chapter> chapters = mangaOptional.get().getChapters();

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
                "data", mangaOptional,
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
                "manga", manga,//Chapter_Length is null
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

        List<UpdateViewDTO> listCurrentWeekly = mangaRepository.getWeekly(7, 0);
        List<UpdateViewDTO> listPreviousWeekly = mangaRepository.getWeekly(14, 7);


        if (listCurrentWeekly.isEmpty()) {
            System.err.println("Current list empty!");
            Map<String, Object> err = Map.of(
                    "msg", "Nothing from weekly mangas ranking!"
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, err).toJSON(), HttpStatus.OK);
        }

        if (listPreviousWeekly.isEmpty()) {
            System.err.println("Previous list empty!");
            listPreviousWeekly = listCurrentWeekly;
            System.out.println("check previous" + listPreviousWeekly);
        }

        List<WeeklyMangaDTO> listWeeklyMangasRanking = new ArrayList<>();


        if (listCurrentWeekly.size() >= listPreviousWeekly.size()) {

            int temp = listCurrentWeekly.size() - listPreviousWeekly.size();

            int previousWeeklySize = listPreviousWeekly.size();
            for (int i = 0; i < listCurrentWeekly.size(); i++) {

                for (int j = 0; j < listPreviousWeekly.size(); j++) {

                    if (listCurrentWeekly.get(i).getManga_id().equals(listPreviousWeekly.get(j).getManga_id())) {

                        WeeklyMangaDTO weeklyMangaDTO = new WeeklyMangaDTO();
                        System.out.println("listCurrentWeekly.get(i).getManga_id()" + listCurrentWeekly.get(i).getManga_id());

                        System.out.println("listCurrentWeekly.get(i).getViews()" + listCurrentWeekly.get(i));
                        System.out.println("listPreviousWeekly.get(j).getViews()" + listPreviousWeekly.get(j).getTotalviews());
                        Long views =
                                listCurrentWeekly.get(i).getTotalviews() - listPreviousWeekly.get(j).getTotalviews();
                        System.out.println(" view sau khi tru" + views);

                        weeklyMangaDTO.setManga_id(listCurrentWeekly.get(i).getManga_id());
                        weeklyMangaDTO.setViews(listCurrentWeekly.get(i).getTotalviews());
                        weeklyMangaDTO.setView_compares(views);
                        System.out.println("check set");

                        listWeeklyMangasRanking.add(weeklyMangaDTO);
                        System.out.println("listWeeklyMangasRanking.set(j,mangaDTO)" + listWeeklyMangasRanking);
                    }
                }

            }
            int z = 0;
            while (z < temp) {

                WeeklyMangaDTO weeklyMangaDTO = new WeeklyMangaDTO();
                weeklyMangaDTO.setManga_id(listCurrentWeekly.get(previousWeeklySize + z).getManga_id());
                weeklyMangaDTO.setViews(listCurrentWeekly.get(previousWeeklySize + z).getTotalviews());
                weeklyMangaDTO.setView_compares(listCurrentWeekly.get(previousWeeklySize + z).getTotalviews());

                listWeeklyMangasRanking.add(weeklyMangaDTO);
                z++;
            }
        } else {

            int previousWeeklySize = listPreviousWeekly.size();

            for (int i = 0; i < previousWeeklySize; i++) {

                for (int j = 0; j < listCurrentWeekly.size(); j++) {

                    if (listPreviousWeekly.get(i).getManga_id().equals(listCurrentWeekly.get(j).getManga_id())) {

                        WeeklyMangaDTO weeklyMangaDTO = new WeeklyMangaDTO();
                        System.out.println("listPreviousWeekly.get(i).getManga_id()");

                        Long views =
                                listCurrentWeekly.get(j).getTotalviews() - listPreviousWeekly.get(i).getTotalviews();

                        weeklyMangaDTO.setManga_id(listCurrentWeekly.get(j).getManga_id());
                        weeklyMangaDTO.setViews(listCurrentWeekly.get(j).getTotalviews());
                        weeklyMangaDTO.setView_compares(views);

                        listWeeklyMangasRanking.add(weeklyMangaDTO);
                    }
                }

            }

        }

        listWeeklyMangasRanking.sort(Comparator.comparing(WeeklyMangaDTO::getView_compares).reversed());
        //   listWeeklyMangasRanking.stream().limit(5L);
        System.out.println("list weekly manga ranking" + listWeeklyMangasRanking);

        List<Manga> listWeeklyRanking = new ArrayList<>();

        List<WeeklyMangaDTO> top5Mangas = listWeeklyMangasRanking.stream().limit(5).collect(Collectors.toList());


        top5Mangas.forEach(item -> {

            Optional<Manga> mangaOptional = mangaRepository.findById(item.getManga_id());
            Manga manga = mangaOptional.get();
            listWeeklyRanking.add(manga);

            System.out.println("totototo" + listWeeklyRanking);

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

    public ResponseEntity getDailyMangas() {

        List<UpdateViewDTO> listCurrentDaily = mangaRepository.getWeekly(1, 0);
        List<UpdateViewDTO> listPreviousDaily = mangaRepository.getWeekly(2, 1);


        if (listCurrentDaily.isEmpty()) {
            System.err.println("Current list empty!");
            Map<String, Object> err = Map.of(
                    "msg", "Nothing from daily mangas ranking!"
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, err).toJSON(), HttpStatus.OK);
        }

        if (listPreviousDaily.isEmpty()) {
            System.err.println("Previous list empty!");
            listPreviousDaily = listCurrentDaily;
            System.out.println("check previous" + listPreviousDaily);
        }

        List<WeeklyMangaDTO> listDailyMangasRanking = new ArrayList<>();


        if (listCurrentDaily.size() >= listPreviousDaily.size()) {

            int temp = listCurrentDaily.size() - listPreviousDaily.size();

            int previousDailySize = listPreviousDaily.size();
            for (int i = 0; i < listCurrentDaily.size(); i++) {

                for (int j = 0; j < listPreviousDaily.size(); j++) {

                    if (listCurrentDaily.get(i).getManga_id().equals(listPreviousDaily.get(j).getManga_id())) {

                        WeeklyMangaDTO weeklyMangaDTO = new WeeklyMangaDTO();

                        Long views = listCurrentDaily.get(i).getTotalviews() - listPreviousDaily.get(j).getTotalviews();
                        System.out.println(" view sau khi tru" + views);

                        weeklyMangaDTO.setManga_id(listCurrentDaily.get(i).getManga_id());
                        weeklyMangaDTO.setViews(listCurrentDaily.get(i).getTotalviews());
                        weeklyMangaDTO.setView_compares(views);
                        System.out.println("check set");

                        listDailyMangasRanking.add(weeklyMangaDTO);
                        System.out.println("listWeeklyMangasRanking.set(j,mangaDTO)" + listDailyMangasRanking);
                    }
                }

            }
            int z = 0;
            while (z < temp) {

                WeeklyMangaDTO dailyMangaDTO = new WeeklyMangaDTO();
                dailyMangaDTO.setManga_id(listCurrentDaily.get(previousDailySize + z).getManga_id());
                dailyMangaDTO.setViews(listCurrentDaily.get(previousDailySize + z).getTotalviews());
                dailyMangaDTO.setView_compares(listCurrentDaily.get(previousDailySize + z).getTotalviews());

                listDailyMangasRanking.add(dailyMangaDTO);
                z++;
            }
        } else {

            int previousDailySize = listPreviousDaily.size();

            for (int i = 0; i < previousDailySize; i++) {

                for (int j = 0; j < listCurrentDaily.size(); j++) {

                    if (listPreviousDaily.get(i).getManga_id().equals(listCurrentDaily.get(j).getManga_id())) {

                        WeeklyMangaDTO dailyMangaDTO = new WeeklyMangaDTO();
                        System.out.println("listPreviousWeekly.get(i).getManga_id()");

                        Long views =
                                listCurrentDaily.get(j).getTotalviews() - listPreviousDaily.get(i).getTotalviews();

                        dailyMangaDTO.setManga_id(listCurrentDaily.get(j).getManga_id());
                        dailyMangaDTO.setViews(listCurrentDaily.get(j).getTotalviews());
                        dailyMangaDTO.setView_compares(views);

                        listDailyMangasRanking.add(dailyMangaDTO);
                    }
                }

            }

        }

        listDailyMangasRanking.sort(Comparator.comparing(WeeklyMangaDTO::getView_compares).reversed());
        //   listWeeklyMangasRanking.stream().limit(5L);
        System.out.println("list daily manga ranking" + listDailyMangasRanking);

        List<Manga> listDailyRanking = new ArrayList<>();

        List<WeeklyMangaDTO> top5Mangas = listDailyMangasRanking.stream().limit(5).collect(Collectors.toList());


        top5Mangas.forEach(item -> {

            Optional<Manga> mangaOptional = mangaRepository.findById(item.getManga_id());
            Manga manga = mangaOptional.get();
            listDailyRanking.add(manga);

            System.out.println("totototo" + listDailyRanking);

        });

        if (listDailyRanking.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "msg", "Nothing from daily mangas ranking empty!"
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, err).toJSON(), HttpStatus.OK);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get daily mangas ranking successfully!",
                "data", listDailyRanking
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


    public ResponseEntity getCommentsManga(Long mangaId, int from, int amount) {

        //get list comments in 1 chapter
        Optional<AuthorMangaDTO> mangaOptional = mangaRepository.getAllByMangaId(mangaId);

        if (mangaOptional.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "Manga not found!"
            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }


        Pageable pageable = new OffsetBasedPageRequest(from, amount);
        List<CommentExportDTO> commentsOfChapters = chapterCommentsRepos.getCommentsManga(mangaId, pageable);

        if (commentsOfChapters.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "No comments found!"
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get chapter comments successfully!",
                "manga_info", mangaOptional,
                "comments", commentsOfChapters
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }

    public ResponseEntity searchMangasByGenres(List<Long> listGenreId) {

        if (listGenreId.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "empty genres!"
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        List<Genre> genresInput = new ArrayList<>();
        List<Genre> genres = genreRepository.findAll();
        genres.forEach(genre -> {
            listGenreId.forEach(genreId -> {
                if (genre.getGenre_id().equals(genreId)) {
                    genresInput.add(genre);
                }
            });
        });


        System.out.println("listGenreId.get (0) " + listGenreId.get(0));
        //Get all genres from manga_genres
        List<MangaGenreDTO> listGenresMangas = genreRepository.getAllGenresMangas();

        //Get all mangas
        List<MangaChapterDTO> Mangas = mangaRepository.getLatestChapterFromManga();

        /////check listGenresMangas
        if (listGenresMangas.isEmpty()) {
            Map<String, Object> msg = Map.of(
                    "msg", "empty genres!"
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }

        //Get and check first genreId from input
        Long firstGenreId = 0L;
        firstGenreId = listGenreId.get(0);
        List<MangaGenreDTO> firstList = new ArrayList<>();
        Long finalFirstGenreId = firstGenreId;

        if (firstGenreId == null || firstGenreId == 0L) {
            Map<String, Object> msg = Map.of(
                    "msg", "empty genres!"
            );
            return new ResponseEntity<>(new Response(200, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }

        listGenresMangas.forEach(items -> {
            if (items.getGenre_id().equals(finalFirstGenreId)) {
                firstList.add(items);
            }
        });

        //check genreId after first genreId.
        List<MangaGenreDTO> subFirstList = firstList;
        Long finalLastGenId = finalFirstGenreId;


        System.out.println("listgensize" + listGenreId.size());
        for (int i = 1; i < listGenreId.size(); i++) {

            Long GenreIdAtI = 0L;
            GenreIdAtI = listGenreId.get(i);
            Long finalGenreId = GenreIdAtI;

            System.out.println("listGenreId.get (1) " + listGenreId.get(i));

            if (GenreIdAtI == null || GenreIdAtI == 0L) {

                List<MangaChapterDTO> mangaChapterDTOList =
                        new AdvancedSearchGenreId(mangaRepository).showMangaList(subFirstList, Mangas);

                if (mangaChapterDTOList.isEmpty()) {

                    Map<String, Object> msg = Map.of(
                            "msg", "Manga not found!"
                    );
                    return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(),
                            HttpStatus.ACCEPTED);
                }

                Map<String, Object> msg = Map.of(
                        "msg", "Get search results successfully!1",
                        "data", mangaChapterDTOList,
                        "genres_info", genresInput
                );
                return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
            }

            List<MangaGenreDTO> secondList = new AdvancedSearchGenreId(mangaRepository).searchGen(finalGenreId,
                    subFirstList, listGenresMangas);

            subFirstList = secondList;
        }


        List<MangaGenreDTO> lastList = subFirstList;
        List<MangaChapterDTO> mangaChapterDTOList = new AdvancedSearchGenreId(mangaRepository).showMangaList(lastList
                , Mangas);

        if (mangaChapterDTOList.isEmpty()) {

            Map<String, Object> msg = Map.of(
                    "msg", "Manga not found!last"
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }
        Map<String, Object> msg = Map.of(
                "msg", "Get search results successfully!last",
                "mangas", mangaChapterDTOList,
                "genres", genresInput
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
