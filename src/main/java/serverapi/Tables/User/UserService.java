package serverapi.Tables.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Query.DTO.UserReadingHistoryDTO;
import serverapi.Query.Repository.ChapterRepos;
import serverapi.Query.Repository.MangaRepos;
import serverapi.Query.Repository.ReadingHistoryRepos;
import serverapi.Query.Repository.UserRepos;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.ReadingHistory.ReadingHistory;
import serverapi.Tables.User.POJO.UserPOJO;

import javax.mail.Session;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final MangaRepos mangaRepos;
    private final ChapterRepos chapterRepos;
    private final UserRepos userRepos;
    private final ReadingHistoryRepos readingHistoryRepos;

    @Autowired
    public UserService(MangaRepos mangaRepos, ChapterRepos chapterRepos, UserRepos userRepos, ReadingHistoryRepos readingHistoryRepos) {
        this.mangaRepos = mangaRepos;
        this.chapterRepos = chapterRepos;
        this.userRepos = userRepos;
        this.readingHistoryRepos = readingHistoryRepos;
    }

    public ResponseEntity GetUserByReadingHistory(Long userId) {


        List<UserReadingHistoryDTO> readingHistoryDTOS = readingHistoryRepos.GetUserByReadingHistor(userId);

       readingHistoryDTOS.stream().sorted(Comparator.comparing(UserReadingHistoryDTO ::getReading_History_time).reversed()).collect(Collectors.toList());



//


        Map<String, Object> msg = Map.of(
                "msg", "Get reading history mangas successfully!",

                "chapterInfo", readingHistoryDTOS
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);


    }

    public ResponseEntity updatetime(Long userId, Long mangaId, UserPOJO userPOJO, Long chapterId) {

        List<UserReadingHistoryDTO> readingHistoryDTO = readingHistoryRepos.GetUserByReadingHistor(userId);
        Calendar updatetime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);

        readingHistoryDTO.forEach(item -> {
            if (item.getManga_id().equals(mangaId)) {
                atomicBoolean.set(true);

                Long readingHistoryId = item.getReadingHistory_id();

                Optional<ReadingHistory> readingHistoryOptional = readingHistoryRepos.findById(readingHistoryId);
                ReadingHistory readingHistory = readingHistoryOptional.get();


                Optional<Chapter> chapterOptional = chapterRepos.findById(chapterId);
                Chapter chapter = chapterOptional.get();


                Optional<Manga> mangaOptional = mangaRepos.findById(mangaId);
                Manga manga = mangaOptional.get();

                readingHistory.setChapter(chapter);
                readingHistory.setReading_history_time(updatetime);

                readingHistoryRepos.save(readingHistory);

            }
        });
        if(atomicBoolean.get() == true){
            Map<String, Object> msg = Map.of(
                    "msg", "Update readinghistory successfully!"

            );
            return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

        }

        Optional<Chapter> chapterOptional = chapterRepos.findById(chapterId);
        Chapter chapter = chapterOptional.get();

        Optional<Manga> mangaOptional = mangaRepos.findById(mangaId);
        Manga manga = mangaOptional.get();

        Optional<User> userOptional = userRepos.findById(userId);
        User user = userOptional.get();

        ReadingHistory readingHistory = new ReadingHistory();

        readingHistory.setUser(user);
        readingHistory.setManga(manga);
        readingHistory.setChapter(chapter);
        readingHistory.setReading_history_time(updatetime);

        readingHistoryRepos.save(readingHistory);

        Map<String, Object> msg = Map.of(
                "msg", "Add readinghistory successfully!"

        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }
}

