package serverapi.Tables.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Query.DTO.FollowingDTO;
import serverapi.Query.DTO.UserReadingHistoryDTO;
import serverapi.Query.Repository.*;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.FollowingManga.FollowingManga;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.ReadingHistory.ReadingHistory;
import serverapi.Tables.User.POJO.UserPOJO;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final MangaRepos mangaRepository;
    private final FollowingRepos followingRepos;
    private final UserRepos userRepos;
    private  final ReadingHistoryRepos readingHistoryRepos;
    private final  ChapterRepos chapterRepos;

    @Autowired
    public UserService(MangaRepos mangaRepository, FollowingRepos followingRepos, UserRepos userRepos, ReadingHistoryRepos readingHistoryRepos, ChapterRepos chapterRepos) {
        this.mangaRepository = mangaRepository;
        this.followingRepos = followingRepos;
        this.userRepos = userRepos;
        this.readingHistoryRepos = readingHistoryRepos;
        this.chapterRepos = chapterRepos;
    }

    public ResponseEntity getFollowManga(Long UserId) {

        List<FollowingDTO> Follow = followingRepos.FindByUserId (UserId);

        if (Follow.isEmpty ()) {
            Map<String, Object> msg = Map.of ("msg", "No mangas!");
            return new ResponseEntity<> (new Response (204, HttpStatus.NO_CONTENT, msg).toJSON (), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of (
                "msg", "Get all mangas successfully!",
                "Following Info", Follow,
                "UserID", UserId

        );
        return new ResponseEntity<> (new Response (200, HttpStatus.OK, msg).toJSON (), HttpStatus.OK);
    }


    public ResponseEntity deleteFollowManga(Long mangaId, Long userId) {
        List<FollowingDTO> Follow = followingRepos.FindByUserId (userId);
        System.out.println ("mangaID" + mangaId);
        System.out.println ("userID" + userId);

        AtomicBoolean atomicBoolean = new AtomicBoolean (false);
        if (Follow.isEmpty ()) {
            Map<String, Object> msg = Map.of ("msg", "No mangas!");
            return new ResponseEntity<> (new Response (204, HttpStatus.NO_CONTENT, msg).toJSON (), HttpStatus.NO_CONTENT);

        } else {

            Follow.forEach (item -> {
                System.out.println (item.getManga_id ());
                System.out.println (item.getManga_id ().equals (mangaId));
                if (item.getManga_id ().equals (mangaId)) {
                    Long FollowId = item.getFollowId ();

                    System.out.println ("follow id: " + FollowId);
                    followingRepos.deleteById (FollowId);
                    atomicBoolean.set (true);
                }
            });
            if (atomicBoolean.get () == true) {
                Map<String, Object> msg = Map.of (
                        "msg", "delete follow successfully!"
                );
                return new ResponseEntity<> (new Response (200, HttpStatus.OK, msg).toJSON (), HttpStatus.OK);
            }

            Map<String, Object> msg = Map.of ("msg", "Cannot delete");
            return new ResponseEntity<> (new Response (204, HttpStatus.NO_CONTENT, msg).toJSON (), HttpStatus.NO_CONTENT);
        }
    }



    public ResponseEntity addFollowManga(Long mangaId, Long userId) {
        AtomicBoolean atomicBoolean = new AtomicBoolean (false);
        List<FollowingDTO> follows = followingRepos.FindByUserId (userId);

        if (follows.isEmpty ()) {

            Optional<User> user = userRepos.findById (userId);
            User user1 = user.get ();

            Optional<Manga> manga = mangaRepository.findById (mangaId);
            Manga manga1 = manga.get ();

            FollowingManga followingManga = new FollowingManga ();
            followingManga.setUser (user1);
            followingManga.setManga (manga1);

            followingRepos.save (followingManga);

            Map<String, Object> msg = Map.of (
                    "msg", "add Follow successfully!"

            );
            return new ResponseEntity<> (new Response (200, HttpStatus.OK, msg).toJSON (), HttpStatus.OK);

        } else {
            follows.forEach (item -> {
                if (item.getManga_id ().equals (mangaId)) {

                    atomicBoolean.set (true);
                }
            });
            if (atomicBoolean.get () == true) {
                Map<String, Object> msg = Map.of ("msg", "Cannot ADD to database!");
                return new ResponseEntity<> (new Response (204, HttpStatus.NO_CONTENT, msg).toJSON (), HttpStatus.NO_CONTENT);

            } else {
                Optional<User> user = userRepos.findById (userId);
                User user1 = user.get ();

                Optional<Manga> manga = mangaRepository.findById (mangaId);
                Manga manga1 = manga.get ();

                FollowingManga followingManga = new FollowingManga ();
                followingManga.setUser (user1);
                followingManga.setManga (manga1);

                followingRepos.save (followingManga);

                Map<String, Object> msg = Map.of (
                        "msg", "add Follow successfully!"
                );
                return new ResponseEntity<> (new Response (200, HttpStatus.OK, msg).toJSON (), HttpStatus.OK);

            }

        }
    }



    public ResponseEntity GetUserByReadingHistory(Long userId) {


        List<UserReadingHistoryDTO> readingHistoryDTOS = readingHistoryRepos.GetUserByReadingHistor(userId);
        readingHistoryDTOS.stream().sorted(Comparator.comparing(UserReadingHistoryDTO::getReading_History_time).reversed()).collect(Collectors.toList());
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


                Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
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

        Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
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

