package serverapi.tables.manga_tables.manga;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.helpers.AdvancedSearchGenreId;
import serverapi.helpers.OffsetBasedPageRequest;
import serverapi.query.dtos.features.*;
import serverapi.query.dtos.features.MangaCommentDTOs.CommentTagsDTO;
import serverapi.query.dtos.features.MangaCommentDTOs.CommentTreesDTO;
import serverapi.query.dtos.features.MangaCommentDTOs.MangaCommentDTOs;
import serverapi.query.dtos.tables.*;
import serverapi.query.repository.manga.ChapterRepos;
import serverapi.query.repository.manga.GenreRepos;
import serverapi.query.repository.manga.MangaRepos;
import serverapi.query.repository.manga.UpdateViewRepos;
import serverapi.query.repository.manga.comment.CommentLikesRepos;
import serverapi.query.repository.manga.comment.CommentTagsRepos;
import serverapi.query.repository.manga.comment.MangaCommentsRepos;
import serverapi.query.specification.Specificationn;
import serverapi.tables.manga_tables.chapter.Chapter;
import serverapi.tables.manga_tables.genre.Genre;
import serverapi.tables.manga_tables.manga.pojo.MangaPOJO;
import serverapi.tables.manga_tables.update_view.UpdateView;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service
public class MangaService {
    private Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    private final MangaRepos mangaRepository;
    private final ChapterRepos chapterRepository;
    private final UpdateViewRepos updateViewRepos;
    private final GenreRepos genreRepository;
    private final MangaCommentsRepos mangaCommentsRepos;
    private final CommentTagsRepos commentTagsRepos;
    private final CommentLikesRepos commentLikesRepos;

    @Autowired
    public MangaService(MangaRepos mangaRepository, ChapterRepos chapterRepository, UpdateViewRepos updateViewRepos,
                        GenreRepos genreRepository, MangaCommentsRepos mangaCommentsRepos,
                        CommentTagsRepos commentTagsRepos, CommentLikesRepos commentLikesRepos) {
        this.mangaRepository = mangaRepository;
        this.chapterRepository = chapterRepository;
        this.updateViewRepos = updateViewRepos;
        this.genreRepository = genreRepository;
        this.mangaCommentsRepos = mangaCommentsRepos;
        this.commentTagsRepos = commentTagsRepos;
        this.commentLikesRepos = commentLikesRepos;
    }

    /**
     * Use for update view chapter when User visit this chapter
     * @param mangaId
     * @param chapterId
     * @param mangaPOJO
     * @return List chapters after update view
     */
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

    /**
     * Finding manga by genre_id
     * @param genreId
     * @return The result of finding
     */
    public ResponseEntity findMangaFromGenre(Long genreId) {

        List<MangaChapterGenreDTO> manga = mangaRepository.findMangaByOneGenre(genreId);

        if (manga.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Cannot get manga from this genre!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of("msg", "Get mangas from this genre successfully!", "data", manga);
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    /**
     * Use for get the latest chapter in each manga
     * @return List of the latest chapter
     */
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

    /**
     * Use for get top 5 manga that have the most views
     * @return list of top 5 manga
     */
    public ResponseEntity getTop() {
        List<Manga> topFiveMangas = mangaRepository.getTop(5);

        if (topFiveMangas.isEmpty()) {
            Map<String, Object> err = Map.of("msg", "Nothing of top mangas!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of("msg", "Get top five mangas successfully!", "data", topFiveMangas);
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    /**
     * Use for get manga information by using manga_id
     * @param mangaId
     * @return manga information include: manga's author, manga's genres, manga's chapters
     */
    public ResponseEntity getMangaPage(Long mangaId) {
        Optional<AuthorMangaDTO> manga = mangaRepository.getMangaInfoByMangaID(mangaId);
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


    /**
     * Update all view manga and set into update_view table
     * @return list manga after updated view
     */
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
            view.setCreated_at(currentTime);
            view.setManga(manga);

            updateViewRepos.save(view);
        });

        Map<String, Object> msg = Map.of(
                "msg", "Get total views mangas successfully!",
                "data", listViewsMangas
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    /**
     * Get weekly manga base on the difference between the current week and the previous week
     * @return list of weekly manga
     */
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

        List<WeeklyMangaDTO> listWeeklyMangasRanking = new ArrayList<>();


        if (listCurrentWeekly.size() >= listPreviousWeekly.size()) {

            int temp = listCurrentWeekly.size() - listPreviousWeekly.size();

            int previousWeeklySize = listPreviousWeekly.size();
            for (int i = 0; i < listCurrentWeekly.size(); i++) {

                for (int j = 0; j < listPreviousWeekly.size(); j++) {

                    if (listCurrentWeekly.get(i).getManga_id().equals(listPreviousWeekly.get(j).getManga_id())) {

                        WeeklyMangaDTO weeklyMangaDTO = new WeeklyMangaDTO();

                        Long views =
                                listCurrentWeekly.get(i).getTotalviews() - listPreviousWeekly.get(j).getTotalviews();


                        weeklyMangaDTO.setManga_id(listCurrentWeekly.get(i).getManga_id());
                        weeklyMangaDTO.setViews(listCurrentWeekly.get(i).getTotalviews());
                        weeklyMangaDTO.setView_compares(views);


                        listWeeklyMangasRanking.add(weeklyMangaDTO);
                        System.err.println("listweekly" + listCurrentWeekly.get(i).getManga_id());
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

                        Long views =
                                listCurrentWeekly.get(j).getTotalviews() - listPreviousWeekly.get(i).getTotalviews();

                        weeklyMangaDTO.setManga_id(listCurrentWeekly.get(j).getManga_id());
                        weeklyMangaDTO.setViews(listCurrentWeekly.get(j).getTotalviews());
                        weeklyMangaDTO.setView_compares(views);

                        listWeeklyMangasRanking.add(weeklyMangaDTO);
                        System.err.println("checklistweekly" + listCurrentWeekly.get(j).getManga_id());
                    }
                }

            }

        }


        listWeeklyMangasRanking.sort(comparing(WeeklyMangaDTO::getView_compares).reversed());
        ////remove duplicate from listWeeklyMangasRanking;
        List<WeeklyMangaDTO> listWeeklyMangasRankingAfterRemoveDuplicates = listWeeklyMangasRanking.stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<WeeklyMangaDTO>(comparing(WeeklyMangaDTO::getManga_id))),
                        ArrayList::new));
        listWeeklyMangasRankingAfterRemoveDuplicates.forEach(item -> {
            System.err.println("listWeeklyMangasRankingAfterRemoveDuplicates" + item.getManga_id());
        });
        listWeeklyMangasRankingAfterRemoveDuplicates.sort(comparing(WeeklyMangaDTO::getView_compares).reversed());


        List<AuthorMangaDTO> listWeeklyRanking = new ArrayList<>();

        List<WeeklyMangaDTO> top10Mangas =
                listWeeklyMangasRankingAfterRemoveDuplicates.stream().limit(10).collect(Collectors.toList());

        List<AuthorMangaDTO> mangas = mangaRepository.getAllMangas();

        if (mangas.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Nothing from daily mangas ranking empty!");
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, err).toJSON(), HttpStatus.OK);
        }

        top10Mangas.forEach(item -> {
            mangas.forEach(manga -> {
                if (item.getManga_id() == manga.getManga_id()) {
                    listWeeklyRanking.add(manga);
                }
            });

            System.err.println("listWeeklyRanking: " + listWeeklyRanking);

        });

        if (listWeeklyRanking.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Nothing from weekly mangas ranking empty!");
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, err).toJSON(), HttpStatus.OK);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get weekly mangas ranking successfully!",
                "list_weekly", listWeeklyRanking
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    /**
     * Get daily manga base on the difference between the current day and the previous day
     * @return list of daily manga
     */
    public ResponseEntity getDailyMangas() {
        List<UpdateViewDTO> listCurrentDaily = mangaRepository.getWeekly(1, 0);
        listCurrentDaily.forEach(item -> {
            System.err.println("item in listcurrent " + item.getManga_id());
            System.err.println("item in listcurrent " + item.getCreated_at().getFirstDayOfWeek());

        });
        System.err.println("datecurrent" + LocalDateTime.now());
        List<UpdateViewDTO> listPreviousDaily = mangaRepository.getWeekly(2, 1);
        listPreviousDaily.forEach(item -> {
            System.err.println("item in lisprev " + item.getManga_id());
            System.err.println("item in listprev " + item.getCreated_at().getFirstDayOfWeek());

        });

        System.err.println("listPreviousDaily check " + listPreviousDaily.isEmpty());
        if (listCurrentDaily.isEmpty()) {
            System.err.println("Current list daily empty!");
            Map<String, Object> err = Map.of("err", "Nothing from daily mangas ranking!");
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, err).toJSON(), HttpStatus.OK);
        }

        List<DailyMangaDTO> listDailyMangasRanking = new ArrayList<>();


        if (listCurrentDaily.size() >= listPreviousDaily.size()) {
            int temp = listCurrentDaily.size() - listPreviousDaily.size();
            int previousDailySize = listPreviousDaily.size();

            for (int i = 0; i < listCurrentDaily.size(); i++) {
                for (int j = 0; j < listPreviousDaily.size(); j++) {

                    if (listCurrentDaily.get(i).getManga_id().equals(listPreviousDaily.get(j).getManga_id())) {

                        DailyMangaDTO dailyMangaDTO = new DailyMangaDTO();

                        Long views = listCurrentDaily.get(i).getTotalviews() - listPreviousDaily.get(j).getTotalviews();
                        System.err.println("Compare " + views);
                        dailyMangaDTO.setManga_id(listCurrentDaily.get(i).getManga_id());
                        dailyMangaDTO.setViews(listCurrentDaily.get(i).getTotalviews());
                        dailyMangaDTO.setView_compares(views);
                        System.out.println("check set");

                        listDailyMangasRanking.add(dailyMangaDTO);
                        System.out.println("listWeeklyMangasRanking.set(j,mangaDTO)" + listDailyMangasRanking);
                    }
                }

            }
            int z = 0;
            while (z < temp) {

                DailyMangaDTO dailyMangaDTO = new DailyMangaDTO();
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

                        DailyMangaDTO dailyMangaDTO = new DailyMangaDTO();
                        System.out.println("listPreviousDaily.get(i).getManga_id()");

                        Long views =
                                listCurrentDaily.get(j).getTotalviews() - listPreviousDaily.get(i).getTotalviews();
                        System.err.println("Compare " + views);

                        dailyMangaDTO.setManga_id(listCurrentDaily.get(j).getManga_id());
                        dailyMangaDTO.setViews(listCurrentDaily.get(j).getTotalviews());
                        dailyMangaDTO.setView_compares(views);

                        listDailyMangasRanking.add(dailyMangaDTO);
                    }
                }

            }

        }

        listDailyMangasRanking.sort(comparing(DailyMangaDTO::getView_compares).reversed());
        ////remove duplicate from listWeeklyMangasRanking;
        List<DailyMangaDTO> listDailyMangasRankingAfterRemoveDuplicates = listDailyMangasRanking.stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<DailyMangaDTO>(comparing(DailyMangaDTO::getManga_id))),
                        ArrayList::new));

        listDailyMangasRankingAfterRemoveDuplicates.forEach(item -> {
            System.err.println("lisDailyMangasRankingAfterRemoveDuplicates" + item.getManga_id());
        });
        listDailyMangasRankingAfterRemoveDuplicates.sort(comparing(DailyMangaDTO::getView_compares).reversed());

        List<AuthorMangaDTO> listDailyRanking = new ArrayList<>();

        List<DailyMangaDTO> top10Mangas = listDailyMangasRankingAfterRemoveDuplicates.stream().limit(10).collect(Collectors.toList());

        List<AuthorMangaDTO> mangas = mangaRepository.getAllMangas();

        if (mangas.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Nothing from daily mangas ranking!");
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, err).toJSON(), HttpStatus.OK);
        }

        top10Mangas.forEach(item -> {
            System.err.println("item iD này " + item.getManga_id());
            mangas.forEach(manga -> {
                if (item.getManga_id() == manga.getManga_id()) {
                    listDailyRanking.add(manga);
                    System.err.println("added");
                }
            });

            System.err.println("listDailyRanking: " + listDailyRanking);

        });

        if (listDailyRanking.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Nothing from daily mangas ranking!");
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, err).toJSON(), HttpStatus.OK);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get daily mangas ranking successfully!",
                "list_daily", listDailyRanking
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    /**
     * Get the suggestion list manga by shuffle the manga list
     * @return suggestion list
     */
    public ResponseEntity getSuggestionList() {
//        List<Manga> mangaList = mangaRepository.getRandomList(5);

        Long totalRows = mangaRepository.count();
        int randomPosition = (int) (Math.random() * totalRows);
        if (randomPosition >= (totalRows - 5)) {
            randomPosition -= 5;
        }
        Pageable pageable = new OffsetBasedPageRequest(randomPosition, 5);
        Page<Manga> mangaPage = mangaRepository.findAll(pageable);

        System.err.println("Getting suggestion list");
        if (mangaPage.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "No manga!",
                    "suggestion_list", mangaPage.getContent()
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get suggestion list successfully!",
                "suggestion_list", mangaPage.getContent()
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    /**
     * Searching manga by using manga_name
     * @param mangaName
     * @return search result
     */
    public ResponseEntity searchMangasByName(String mangaName) {
        Specificationn specificationn = new Specificationn(new SearchCriteriaDTO("manga_name", ":", mangaName));
        Specificationn.SearchingManga searchingManga = specificationn.new SearchingManga();

        List<Manga> searchingResults = mangaRepository.findAll(searchingManga);

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

    /**
     * Searching manga by using multiple genres
     * @param listGenreId
     * @return search result
     */
    public ResponseEntity searchMangasByGenres(@NotNull List<Long> listGenreId) {

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


        for (int i = 1; i < listGenreId.size(); i++) {

            Long GenreIdAtI = 0L;
            GenreIdAtI = listGenreId.get(i);
            Long finalGenreId = GenreIdAtI;

            System.out.println("listGenreId.get (1) " + listGenreId.get(i));

            if (GenreIdAtI == null || GenreIdAtI == 0L) {

                List<MangaChapterDTO> mangaChapterDTOList =
                        new AdvancedSearchGenreId(mangaRepository).showMangaList(subFirstList, Mangas);

                if (mangaChapterDTOList.isEmpty()) {
                    Map<String, Object> err = Map.of("err", "Manga not found!");
                    return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(),
                            HttpStatus.ACCEPTED);
                }

                Map<String, Object> msg = Map.of(
                        "msg", "Get search results successfully!",
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
        List<MangaChapterDTO> mangaChapterDTOList = new AdvancedSearchGenreId(mangaRepository).showMangaList(lastList, Mangas);
        if (mangaChapterDTOList.isEmpty()) {
            Map<String, Object> err = Map.of("err", "Manga not found");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }


        Map<String, Object> msg = Map.of(
                "msg", "Get search results successfully!",
                "mangas", mangaChapterDTOList,
                "genres", genresInput
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    /**
     * Get manga's comments by using manga_id, pageable
     * @param mangaId
     * @param from
     * @param amount
     * @return manga's comments
     */
    public ResponseEntity getCommentsManga(Long mangaId, int from, int amount) {

        // Initialize variable
        final String level1 = "1";
        final String level2 = "2";

        final Pageable pageable = new OffsetBasedPageRequest(from, amount);
        final Pageable childPageable = new OffsetBasedPageRequest(0, 5);

        // Check this manga is null or not
        Optional<AuthorMangaDTO> mangaOptional = mangaRepository.getMangaInfoByMangaID(mangaId);
        if (mangaOptional.isEmpty()) {

            Map<String, Object> err = Map.of("err", "Manga not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        // get manga comments in each level
        List<MangaCommentDTOs> cmtsLv0 = mangaCommentsRepos.getMangaCommentsLevel0(mangaId, pageable);
        if (cmtsLv0.isEmpty()) {

            Map<String, Object> msg = Map.of("msg", "No comments found!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }

        // Get comment
        //set tags for each comment
        List<MangaCommentDTOs> comments;
        cmtsLv0.forEach(lv0 ->{

            lv0 = setListTags(lv0);

            //get child comments
            List<CommentTreesDTO> cmtsLv1 = mangaCommentsRepos.getCommentsChild(lv0.getManga_comment_id(), level1,childPageable);
            List<CommentTreesDTO> cmtsLv2 = mangaCommentsRepos.getCommentsChild(lv0.getManga_comment_id(), level2, childPageable);

            MangaCommentDTOs finalLv0 = lv0;
            cmtsLv1.forEach(lv01 ->{

                CommentTreesDTO finalLv01 = lv01;
                cmtsLv2.forEach(lv02 ->{

                    lv02 = setListTags(lv02);

                    if(finalLv0.getManga_comment_id() == lv02.getParent_id()){

                        finalLv01.getComments_level_02().add(lv02);
                    }
                });

                lv01 = setListTags(lv01);

                if(finalLv0.getManga_comment_id() == lv01.getParent_id()){

                    finalLv0.getComments_level_01().add(lv01);
                }
            });
        });
        comments = cmtsLv0;

        Map<String, Object> msg = Map.of(
                "msg", "Get manga's comments successfully!",
                "manga_info", mangaOptional,
                "don't use these param","manga_comment_relation_id, parent_id, child_id, level, manga_comment_tag_id",
                "comments", comments
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }

    public ResponseEntity getChildComments(Long commentID, int from, int amount) {

        // Initialize variable
        final String level1 = "1";
        final String level2 = "2";

        final Pageable pageable = new OffsetBasedPageRequest(from, amount);

        // Check commentID
        Optional<MangaCommentDTOs> mangaCommentOptional = mangaCommentsRepos.findByCommentID(commentID);
        if (mangaCommentOptional.isEmpty()) {

            Map<String, Object> err = Map.of("err", "Comment not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        MangaCommentDTOs mangaComment = mangaCommentOptional.get();

        // Get comment
        //set tags for comment
        mangaComment = setListTags(mangaComment);

            //get child comments
            List<CommentTreesDTO> cmtsLv1 = mangaCommentsRepos.getCommentsChild(mangaComment.getManga_comment_id(), level1,pageable);
            List<CommentTreesDTO> cmtsLv2 = mangaCommentsRepos.getCommentsChild(mangaComment.getManga_comment_id(), level2, pageable);

            MangaCommentDTOs finalMangaComment = mangaComment;
            cmtsLv1.forEach(lv01 ->{

                CommentTreesDTO finalLv01 = lv01;
                cmtsLv2.forEach(lv02 ->{

                    lv02 = setListTags(lv02);

                    if(finalMangaComment.getManga_comment_id() == lv02.getParent_id()){

                        finalLv01.getComments_level_02().add(lv02);
                    }
                });

                lv01 = setListTags(lv01);

                if(finalMangaComment.getManga_comment_id() == lv01.getParent_id()){

                    finalMangaComment.getComments_level_01().add(lv01);
                }
            });


        Map<String, Object> msg = Map.of(
                "msg", "Get child's comments successfully!",
                "don't use these param","manga_comment_relation_id, parent_id, child_id, level, manga_comment_tag_id",
                "comments", mangaComment
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }


    ///////////// sub functions /////////////////

    public MangaCommentDTOs setListTags(MangaCommentDTOs mangaCommentDTOs){

        List<CommentTagsDTO> tags = commentTagsRepos.getListTags(mangaCommentDTOs.getManga_comment_id());

        if(!tags.isEmpty()){
            mangaCommentDTOs.setTo_users(tags);
        }

        return mangaCommentDTOs;
    }
    public CommentTreesDTO setListTags(CommentTreesDTO commentTreesDTO){

        List<CommentTagsDTO> tags = commentTagsRepos.getListTags(commentTreesDTO.getManga_comment_id());

        if(!tags.isEmpty()){
            commentTreesDTO.setTo_users(tags);
        }

        return commentTreesDTO;
    }

}
