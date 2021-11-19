package serverapi.tables.user_tables.user.user_unauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.tables.comment.comment_like.CommentLikeService;
import serverapi.tables.user_tables.user.friend.FriendService;
import serverapi.tables.user_tables.user.user.UserService;

import java.util.Map;


@RestController
@RequestMapping("/api/user_unauth")
@CacheConfig(cacheNames = {"user_unauth"})
public class UserControllerUnAuth {

    private final UserUnauthService userUnauthService;
    private final UserService userService;
    private final CommentLikeService commentLikeService;
    private final FriendService friendService;

    @Autowired
    public UserControllerUnAuth(UserService userService, UserUnauthService userUnauthService, CommentLikeService commentLikeService, FriendService friendService) {
        this.userService = userService;
        this.userUnauthService = userUnauthService;
        this.commentLikeService = commentLikeService;
        this.friendService = friendService;
    }


    ///////////////////////////////// Users parts //////////////////////////////
    @PostMapping("/searchusers")
    public ResponseEntity searchUsers(@RequestBody Map data) {
        String valToSearch = (String) data.get("value");
        int key = (int) data.get("key"); // search with: 1 is email, 2 is name

        return userService.searchUsers(valToSearch, key);
    }

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

        return friendService.getListFriends(userId, from, amount);
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

    @PostMapping("/get_total_friend")
    public ResponseEntity getTotalFriend(@RequestBody Map data) {
        String user_id = String.valueOf(data.get("user_id"));

        Long userID = 0L;
        if (!user_id.isEmpty()) {
            userID = Long.parseLong(user_id);
        }

        return friendService.getTotalFriend(userID);
    }

}