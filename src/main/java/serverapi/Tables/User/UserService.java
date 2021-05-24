package serverapi.Tables.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.Api.Response;
import serverapi.Query.DTO.ChapterCommentsDTO;
import serverapi.Query.DTO.FollowingDTO;
import serverapi.Query.DTO.UserReadingHistoryDTO;
import serverapi.Query.Repository.*;
import serverapi.Tables.Chapter.Chapter;
import serverapi.Tables.FollowingManga.FollowingManga;
import serverapi.Tables.Manga.Manga;
import serverapi.Tables.ReadingHistory.ReadingHistory;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class UserService {
    private final MangaRepos mangaRepository;
    private final FollowingRepos followingRepos;
    private final UserRepos userRepos;
    private final ReadingHistoryRepos readingHistoryRepos;
    private final ChapterRepos chapterRepos;
    private final ChapterCommentsRepos chapterCommentsRepos;

    @Autowired
    public UserService(MangaRepos mangaRepository, FollowingRepos followingRepos, UserRepos userRepos,
                       ReadingHistoryRepos readingHistoryRepos, ChapterRepos chapterRepos, ChapterCommentsRepos chapterCommentsRepos) {
        this.mangaRepository = mangaRepository;
        this.followingRepos = followingRepos;
        this.userRepos = userRepos;
        this.readingHistoryRepos = readingHistoryRepos;
        this.chapterRepos = chapterRepos;
        this.chapterCommentsRepos=chapterCommentsRepos;
    }

    public ResponseEntity getFollowManga(Long UserId) {

        List<FollowingDTO> followingDTOList = followingRepos.FindByUserId(UserId);

        if(followingDTOList.isEmpty ()){

            Map<String, Object> msg = Map.of("msg", "No manga follow found!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        Map<String, Object> msg = Map.of(
                "msg", "Get following mangas successfully!",
                "mangas", followingDTOList,
                "user_id", UserId

        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity getChapterComments(Long userId) {

        List<ChapterCommentsDTO> chapterCommentsDTOList = chapterCommentsRepos.getCommentsByUserId (userId);

        if(chapterCommentsDTOList.isEmpty ()){
            Map<String, Object> msg = Map.of("msg", "No comment found!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get chapter comment successfully!",
                "mangas", chapterCommentsDTOList,
                "user_id", userId

        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity deleteFollowManga(Long mangaId, Long userId) {
        List<FollowingDTO> Follow = followingRepos.FindByUserId(userId);
        System.out.println("mangaID" + mangaId);
        System.out.println("userID" + userId);

        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        if (Follow.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "No mangas!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);

        } else {

            Follow.forEach(item -> {
                System.out.println(item.getManga_id());
                System.out.println(item.getManga_id().equals(mangaId));
                if (item.getManga_id().equals(mangaId)) {
                    Long FollowId = item.getFollowId();

                    System.out.println("follow id: " + FollowId);
                    followingRepos.deleteById(FollowId);
                    atomicBoolean.set(true);
                }
            });
            if (atomicBoolean.get() == true) {
                Map<String, Object> msg = Map.of(
                        "msg", "delete follow successfully!"
                );
                return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
            }

            Map<String, Object> msg = Map.of("msg", "Cannot delete");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
    }


    public ResponseEntity addFollowManga(Long mangaId, Long userId) {
        AtomicBoolean atomicBoolean = new AtomicBoolean(false);
        List<FollowingDTO> follows = followingRepos.FindByUserId(userId);

        if (follows.isEmpty()) {
            Optional<User> userOptional = userRepos.findById(userId);
            User user = userOptional.get();

            Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
            Manga manga = mangaOptional.get();

            FollowingManga followingManga = new FollowingManga();
            followingManga.setUser(user);
            followingManga.setManga(manga);

            followingRepos.save(followingManga);

            Map<String, Object> msg = Map.of(
                    "msg", "add Follow successfully!"

            );
            return new ResponseEntity<>(new Response(201, HttpStatus.CREATED, msg).toJSON(), HttpStatus.CREATED);

        } else {
            follows.forEach(item -> {
                if (item.getManga_id().equals(mangaId)) {

                    atomicBoolean.set(true);
                }
            });
            if (atomicBoolean.get() == true) {
                Map<String, Object> err = Map.of("err", "Cannot ADD to database!");
                return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                        HttpStatus.BAD_REQUEST);

            } else {
                Optional<User> userOptional = userRepos.findById(userId);
                User user = userOptional.get();

                Optional<Manga> mangaOptional = mangaRepository.findById(mangaId);
                Manga manga = mangaOptional.get();

                FollowingManga followingManga = new FollowingManga();
                followingManga.setUser(user);
                followingManga.setManga(manga);

                followingRepos.save(followingManga);

                Map<String, Object> msg = Map.of(
                        "msg", "add follow successfully!"
                );
                return new ResponseEntity<>(new Response(201, HttpStatus.CREATED, msg).toJSON(), HttpStatus.CREATED);

            }

        }
    }


    public ResponseEntity GetReadingHistory(Long userId) {
        List<UserReadingHistoryDTO> readingHistoryDTO = readingHistoryRepos.GetHistoriesByUserId (userId);
        readingHistoryDTO.sort(Comparator.comparing(UserReadingHistoryDTO::getReading_History_time).reversed());


        Map<String, Object> msg = Map.of(
                "msg", "Get reading history mangas successfully!",
                "mangas", readingHistoryDTO
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity updateReadingHistory(Long userId, Long mangaId, Long chapterId) {

        List<UserReadingHistoryDTO> readingHistoryDTO = readingHistoryRepos.GetHistoriesByUserId (userId);
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

        if (atomicBoolean.get() == true) {
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
                "msg", "Add reading history successfully!"

        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity DeleteUserById(Long userId){
        //get user info
        Optional<User> userOptional = userRepos.findById (userId);

        if(userOptional.isEmpty ()){

            Map<String, Object> msg = Map.of(
                    "msg", "user not found!"

            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get ();
        userRepos.delete (user);

        Map<String, Object> msg = Map.of(
                "msg", "delete user successfully!",
                "user deleted info: ",user

        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }


    public ResponseEntity DeprecateUserById(Long userId){
        //get user info
        Optional<User> userOptional = userRepos.findById (userId);

        if(userOptional.isEmpty ()){

            Map<String, Object> msg = Map.of(
                    "msg", "user not found!"

            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get ();
        user.setUser_isVerified (false);
        userRepos.save (user);

        Map<String, Object> msg = Map.of(
                "msg", "Deprecate user successfully!",
                "user deprecate info: ",user

        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }


    public ResponseEntity DeleteFollowsUsersByUserId(Long userId){
        List<FollowingDTO> followingDTOList = followingRepos.FindByUserId(userId);
        Optional<User> userOptional = userRepos.findById (userId);

        if(userOptional.isEmpty ()){

            Map<String, Object> msg = Map.of(
                    "msg", "user not found!"

            );
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }if(followingDTOList.isEmpty ()){
            Map<String, Object> msg = Map.of("msg", "No Follows!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        User user = userOptional.get ();
        followingRepos.deleteAllFollowByUserId (user);

        Map<String, Object> msg = Map.of(
                "msg", "delete all user's follows successfully!",
                "user deleted info: ",user
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity DeleteHitoriesUsersByUserId(Long userId){

        List<UserReadingHistoryDTO> userReadingHistoryDTOList = readingHistoryRepos.GetHistoriesByUserId (userId);
        Optional<User> userOptional = userRepos.findById (userId);

        if(userOptional.isEmpty ()){

            Map<String, Object> msg = Map.of(
                    "msg", "user not found!"

            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }if(userReadingHistoryDTOList.isEmpty ()){
            Map<String, Object> msg = Map.of("msg", "No history found!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        User user = userOptional.get ();
        readingHistoryRepos.deleteAllHistoryByUserId (user);

        Map<String, Object> msg = Map.of(
                "msg", "delete all user's histories successfully!",
                "user deleted info: ",user
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity DeleteCommentsUsersByUserId(Long userId){

        List<ChapterCommentsDTO> chapterCommentsDTOList = chapterCommentsRepos.getCommentsByUserId (userId);
        Optional<User> userOptional = userRepos.findById (userId);

        if(userOptional.isEmpty ()){

            Map<String, Object> msg = Map.of(
                    "msg", "user not found!"

            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);

        }if(chapterCommentsDTOList.isEmpty ()){

            Map<String, Object> msg = Map.of("msg", "No comment found!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        User user = userOptional.get ();
        chapterCommentsRepos.deleteAllCommentsByUserId (user);

        Map<String, Object> msg = Map.of(
                "msg", "delete all user's comments successfully!",
                "user deleted info: ",user
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}

