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

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserSharedService {

    private final MangaRepos mangaRepository;
    private final FollowingRepos followingRepos;
    private final UserRepos userRepos;
    private final ReadingHistoryRepos readingHistoryRepos;
    private final ChapterRepos chapterRepos;
    private final ChapterCommentsRepos chapterCommentsRepos;

    @Autowired
    public UserSharedService(MangaRepos mangaRepository, FollowingRepos followingRepos, UserRepos userRepos,
                       ReadingHistoryRepos readingHistoryRepos, ChapterRepos chapterRepos,
                       ChapterCommentsRepos chapterCommentsRepos) {
        this.mangaRepository = mangaRepository;
        this.followingRepos = followingRepos;
        this.userRepos = userRepos;
        this.readingHistoryRepos = readingHistoryRepos;
        this.chapterRepos = chapterRepos;
        this.chapterCommentsRepos = chapterCommentsRepos;
    }


    public ResponseEntity DeleteFollowsUsersByUserId(Long userId) {
        List<FollowingDTO> followingDTOList = followingRepos.findByUserId(userId);
        Optional<User> userOptional = userRepos.findById(userId);

        if (userOptional.isEmpty()) {

            Map<String, Object> msg = Map.of(
                    "msg", "user not found!"

            );
            return new ResponseEntity<>(new Response (400, HttpStatus.BAD_REQUEST, msg).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }
        if (followingDTOList.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "No Follows!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        User user = userOptional.get();
        followingRepos.deleteAllFollowByUserId(user);

        Map<String, Object> msg = Map.of(
                "msg", "delete all user's follows successfully!",
                "user deleted info: ", user
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity DeleteHitoriesUsersByUserId(Long userId) {

        List<UserReadingHistoryDTO> userReadingHistoryDTOList = readingHistoryRepos.GetHistoriesByUserId(userId);
        Optional<User> userOptional = userRepos.findById(userId);

        if (userOptional.isEmpty()) {

            Map<String, Object> msg = Map.of(
                    "msg", "user not found!"

            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        if (userReadingHistoryDTOList.isEmpty()) {
            Map<String, Object> msg = Map.of("msg", "No history found!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        User user = userOptional.get();
        readingHistoryRepos.deleteAllHistoryByUserId(user);

        Map<String, Object> msg = Map.of(
                "msg", "delete all user's histories successfully!",
                "user deleted info: ", user
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    public ResponseEntity DeleteCommentsUsersByUserId(Long userId) {

        List<ChapterCommentsDTO> chapterCommentsDTOList = chapterCommentsRepos.getCommentsByUserId(userId);
        Optional<User> userOptional = userRepos.findById(userId);

        if (userOptional.isEmpty()) {

            Map<String, Object> msg = Map.of(
                    "msg", "user not found!"

            );
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);

        }
        if (chapterCommentsDTOList.isEmpty()) {

            Map<String, Object> msg = Map.of("msg", "No comment found!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, msg).toJSON(), HttpStatus.NO_CONTENT);
        }
        User user = userOptional.get();
        chapterCommentsRepos.deleteAllCommentsByUserId(user);

        Map<String, Object> msg = Map.of(
                "msg", "delete all user's comments successfully!",
                "user deleted info: ", user
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
