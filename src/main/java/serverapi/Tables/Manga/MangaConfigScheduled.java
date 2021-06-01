package serverapi.Tables.Manga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import serverapi.Query.Repository.MangaRepos;
import serverapi.Query.Repository.UpdateViewRepos;

@Configuration
@EnableScheduling
public class MangaConfigScheduled {

    MangaRepos mangaRepository;
    UpdateViewRepos updateViewRepos;

    @Autowired
    public MangaConfigScheduled(MangaRepos mangaRepository, UpdateViewRepos updateViewRepos){
        this.mangaRepository = mangaRepository;
        this.updateViewRepos = updateViewRepos;
    }


//    @Async
//    @Scheduled(cron = "* */12 * * * ?")
//public ResponseEntity getTotalView() {
//    List<MangaViewDTO> listViewsMangas = mangaRepository.getTotalView();
//
//    if (listViewsMangas.isEmpty()) {
//        Map<String, Object> err = Map.of("msg", "Nothing from total views mangas successfully!");
//        return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
//    }
//
//    listViewsMangas.forEach(item -> {
//        Long mangaId = item.getManga_id();
//        Long totalViews = item.getViews();
//        Calendar createdAt = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//
//        Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
//
//        Manga manga = mangaOptional.get();
//
//        if (totalViews.equals(0L)) {
//            manga.setViews(0L);
//            mangaRepository.save(manga);
//        } else {
//            manga.setViews(totalViews);
//            mangaRepository.saveAndFlush(manga);
//        }
//
//        UpdateView view = new UpdateView();
//        view.setTotalviews(totalViews);
//        view.setCreatedAt(createdAt);
//        view.setManga(manga);
//
//        updateViewRepos.save(view);
//    });
//
//    Map<String, Object> msg = Map.of(
//            "msg", "Get total views mangas successfully!",
//            "data", listViewsMangas
//    );
//    return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
//}

}
