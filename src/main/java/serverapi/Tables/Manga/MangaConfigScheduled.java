package serverapi.Tables.Manga;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import serverapi.Queries.Repositories.MangaRepos;
import serverapi.Queries.Repositories.UpdateViewRepos;

@Configuration
@EnableScheduling
public class MangaConfigScheduled {

    MangaRepos mangaRepos;
    UpdateViewRepos updateViewRepos;

    @Autowired
    public MangaConfigScheduled(MangaRepos mangaRepos, UpdateViewRepos updateViewRepos){
        this.mangaRepos = mangaRepos;
        this.updateViewRepos = updateViewRepos;
    }


//    @Async
//    @Scheduled(cron = "* 12 * * * ?")
//    public void setTotalView() {
//        List<MangaViewDTO> listViewsMangas = mangaRepos.getTotalView();
//
//        if (listViewsMangas.isEmpty()) {
//            System.out.println("Cannot schedule task Set Total Views for Mangas!");
//            return;
//        }
//
//        listViewsMangas.forEach(item -> {
//            Long mangaId = item.getManga_id();
//            Long totalViews = item.getViews();
//            Calendar createdAt = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//
//            Manga manga = new Manga();
//            manga.setManga_id(mangaId);
//
//            UpdateView view = new UpdateView();
//            view.setTotalviews(totalViews);
//            view.setCreatedAt(createdAt);
//            view.setManga(manga);
//
//            updateViewRepos.save(view);
//        });
//
//
//        System.out.println("Done schedule task Set Total Views for Mangas: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime()));
//        return;
//    }

}
