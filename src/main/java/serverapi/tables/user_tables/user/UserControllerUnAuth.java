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

    private final UserUnauthService userUnauthService;
    private final CommentLikeService commentLikeService;

    @Autowired
    public UserControllerUnAuth(UserUnauthService userUnauthService, CommentLikeService commentLikeService) {
        this.userUnauthService = userUnauthService;
        this.commentLikeService = commentLikeService;
    }


    ///////////////////////////////// Users parts //////////////////////////////
    @GetMapping("/get_userinfo")
    public ResponseEntity getUserInfo(@RequestParam String user_id) {
        Long userId = Long.parseLong(user_id);

        return userUnauthService.getUserInfo(userId);
    }

    @GetMapping("/get_posts_of_user")
    public ResponseEntity getPostOfUser(@RequestParam String user_id, @RequestParam int from, @RequestParam int amount) {
        Long userId = Long.parseLong(user_id);

        return userUnauthService.getPostOfUser(userId, from, amount);
    }

    @GetMapping("/get_friends_of_user")
    public ResponseEntity getFriendsOfUser(@RequestParam String user_id, @RequestParam int from, @RequestParam int amount) {
        Long userId = Long.parseLong(user_id);

        return userUnauthService.getFriendsOfUser(userId, from, amount);
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