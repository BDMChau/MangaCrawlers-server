package serverapi.tables.forum.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.api.Response;
import serverapi.tables.forum.post.pojo.PostPOJO;
import serverapi.tables.manga_tables.genre.Genre;
import serverapi.tables.manga_tables.manga.pojo.CommentPOJO;
import serverapi.tables.user_tables.notification.NotificationController;
import serverapi.utils.UserHelpers;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/forum/post")
@CacheConfig(cacheNames = {"post"})
public class PostController {
    private UserHelpers userHelpers = new UserHelpers();

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping("/create")
    public ResponseEntity createPost(ServletRequest request, @RequestBody PostPOJO postPOJO) {
            Long userId = Long.parseLong(userHelpers.getUserAttribute(request).get("user_id").toString());

            String title = postPOJO.getTitle();
            String content = postPOJO.getContent();

            List<Long> listCategoryId = new ArrayList<>();
            postPOJO.getCategoriesId().forEach(id -> {
                listCategoryId.add(Long.parseLong(id));
            });

            return postService.createPost(userId, title, content, listCategoryId);
    }

    @PostMapping("/check_user_like")
    public ResponseEntity checkUserLike(@RequestBody Map data, ServletRequest request) {
        String post_id = String.valueOf(data.get("post_id"));

        Long userID;
        Long postID = 0L;
        String sUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        if (sUserId.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "User empty!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }
        userID = Long.parseLong(sUserId);
        if (!post_id.isEmpty()) {
            postID = Long.parseLong(post_id);
        }

        return postService.checkUserLike(userID, postID);
    }


    @PostMapping("/add_like")
    public ResponseEntity addLike(@RequestBody Map data, ServletRequest request) {
        String post_id = String.valueOf(data.get("post_id"));

        Long userID = 0L;
        Long postID = 0L;
        String sUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        if (!sUserId.isEmpty()) {
            userID = Long.parseLong(sUserId);
        }
        if (!post_id.isEmpty()) {
            postID = Long.parseLong(post_id);
        }

        return postService.addLike(userID, postID);
    }


    @PostMapping("/unlike")
    public ResponseEntity unlike(@RequestBody Map data, ServletRequest request) {
        String post_id = String.valueOf(data.get("post_id"));

        Long userID = 0L;
        Long postID = 0L;
        String sUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        if (!sUserId.isEmpty()) {
            userID = Long.parseLong(sUserId);
        }
        if (!post_id.isEmpty()) {
            postID = Long.parseLong(post_id);
        }

        return postService.unlike(userID, postID);
    }

    @PostMapping("/add_dislike")
    public ResponseEntity addDislike(@RequestBody Map data, ServletRequest request) {
        String post_id = String.valueOf(data.get("post_id"));

        Long userID = 0L;
        Long postID = 0L;
        String sUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        if (!sUserId.isEmpty()) {
            userID = Long.parseLong(sUserId);
        }
        if (!post_id.isEmpty()) {
            postID = Long.parseLong(post_id);
        }

        return postService.addDislike(userID, postID);
    }

    @PostMapping("/undislike")
    public ResponseEntity undislike(@RequestBody Map data, ServletRequest request) {
        String post_id = String.valueOf(data.get("post_id"));

        Long userID = 0L;
        Long postID = 0L;
        String sUserId = userHelpers.getUserAttribute(request).get("user_id").toString();
        if (!sUserId.isEmpty()) {
            userID = Long.parseLong(sUserId);
        }
        if (!post_id.isEmpty()) {
            postID = Long.parseLong(post_id);
        }

        return postService.undislike(userID, postID);
    }
}
