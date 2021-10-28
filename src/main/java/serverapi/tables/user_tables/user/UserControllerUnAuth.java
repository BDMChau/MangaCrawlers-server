package serverapi.tables.user_tables.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.tables.user_tables.user.comment_like.CommentLikeService;

import java.util.Map;


@RestController
@RequestMapping("/api/user_unauth")
@CacheConfig(cacheNames = {"user_unauth"})
public class UserControllerUnAuth {

    private final UserService userService;
    private final CommentLikeService commentLikeService;

    @Autowired
    public UserControllerUnAuth(UserService userService, CommentLikeService commentLikeService) {
        this.userService = userService;
        this.commentLikeService = commentLikeService;
    }


    ///////////////////////////////// Users parts //////////////////////////////
    @PostMapping("/get_userinfo")
    public ResponseEntity GetReadingHistory(@RequestBody Map data) {
        Long userId = Long.parseLong(String.valueOf(data.get("user_id")));

        return userService.getUserInfo(userId);
    }

    @PostMapping("/get_total_like")
    public ResponseEntity getTotalLike(@RequestBody Map data) {
        String comment_id = String.valueOf(data.get("comment_id"));

        Long commentID = 0L;
        if (!comment_id.isEmpty()) {
            commentID = Long.parseLong(comment_id);
        }

        return commentLikeService.getTotalLike(commentID);
    }

}