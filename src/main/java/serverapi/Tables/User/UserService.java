package serverapi.Tables.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Query.Repository.ChapterRepos;
import serverapi.Query.Repository.MangaRepos;
import serverapi.Query.Repository.ReadingHistoryRepos;
import serverapi.Query.Repository.UserRepos;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.ReadingHistory.ReadingHistory;

import java.util.*;

@Service
public class UserService {

    private final MangaRepos mangaRepos;
    private final ChapterRepos chapterRepos;
    private final UserRepos userRepos;
    private  final ReadingHistoryRepos readingHistoryRepos;

    @Autowired
    public UserService(MangaRepos mangaRepos, ChapterRepos chapterRepos, UserRepos userRepos, ReadingHistoryRepos readingHistoryRepos) {
        this.mangaRepos = mangaRepos;
        this.chapterRepos = chapterRepos;
        this.userRepos = userRepos;
        this.readingHistoryRepos = readingHistoryRepos;
    }

    public ResponseEntity updatetime(Long mangaId, Long chapterId, Long userId){

        Optional<Manga> mangaOptional = mangaRepos.findById(mangaId);
        Optional<Chapter> chapterOptional =chapterRepos.findById(chapterId);
        Optional<User> userOptional = userRepos.findById(userId);



        List<ReadingHistory> readingHistories = (List<ReadingHistory>) chapterOptional.get().getChapter_id();
        readingHistories.forEach(item->{
            Calendar readingtime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

            if(item.getManga().getManga_id().equals(mangaId) && item.getChapter().getChapter_id().equals(chapterId) && item.getUser().getUser_id().equals(userId)){

                if(item.getReadingHistory_id().equals(readingtime)){
                    ReadingHistory readingHistory = new ReadingHistory();
                    readingHistory.getReading_history_time();
                    readingHistory.setReading_history_time(readingtime);


                    readingHistoryRepos.save(readingHistory);



                }

            }
        });




        Map<String, Object> msg = Map.of(
                "msg", "Get total views mangas successfully!",
                "mangaInfo", manga,
                "chapterInfo",readingHistories
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }
}
