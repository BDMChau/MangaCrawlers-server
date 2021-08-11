package serverapi.tables.manga_tables.manga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import serverapi.query.dtos.tables.MangaViewDTO;
import serverapi.query.repository.manga.MangaRepos;
import serverapi.query.repository.manga.UpdateViewRepos;
import serverapi.tables.manga_tables.update_view.UpdateView;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.TimeZone;

@Configuration
@EnableScheduling
@EnableAsync
public class MangaConfigSchedule {

    MangaRepos mangaRepository;
    UpdateViewRepos updateViewRepos;

    @Autowired
    public MangaConfigSchedule(MangaRepos mangaRepository, UpdateViewRepos updateViewRepos) {
        this.mangaRepository = mangaRepository;
        this.updateViewRepos = updateViewRepos;
    }


//    @Async
////    @Scheduled(cron = "* * */23 * * ?") // every 23 hours
//    @Scheduled(fixedRate = 86400000)
    public void updateViewsToManga() {
        List<MangaViewDTO> listViewsMangas = mangaRepository.getTotalView();

        if (listViewsMangas.isEmpty()) {
            Calendar calendar = Calendar.getInstance();
            System.err.println("Failed to updating views manga: " + calendar.getTime());
            return;
        }

        listViewsMangas.forEach(item -> {
            Long mangaId = item.getManga_id();
            Long totalViews = item.getViews();
            Calendar created_at = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

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
            view.setCreated_at(created_at);
            view.setManga(manga);

            updateViewRepos.saveAndFlush(view);
        });

        Calendar calendar = Calendar.getInstance();
        System.err.println("Updated views manga every 23 hours: " + calendar.getTime());
    }

}
