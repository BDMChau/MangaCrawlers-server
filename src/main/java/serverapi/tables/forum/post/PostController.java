package serverapi.tables.forum.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serverapi.api.Response;
import serverapi.tables.forum.post.pojo.PostPOJO;
import serverapi.utils.UserHelpers;

import javax.servlet.ServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/forum/post")
@CacheConfig(cacheNames = {"post"})
public class PostController {
    private final UserHelpers userHelpers = new UserHelpers();

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

    @PostMapping("/un_dislike")
    public ResponseEntity undislike(@RequestBody Map data, ServletRequest request) {
        String post_id = String.valueOf(data.get("post_id"));

        Long userID = 0L;
        Long postID = 0L;
        String sUserId = userHelpers.getUserAttribute(request).get("user_id").toString();

        if (!sUserId.isEmpty()) userID = Long.parseLong(sUserId);
        if (!post_id.isEmpty()) postID = Long.parseLong(post_id);

        return postService.undislike(userID, postID);
    }


    @PostMapping("/remove")
    public ResponseEntity removePost(@RequestBody Map data, ServletRequest request) {
        Long postId = Long.parseLong(String.valueOf(data.get("post_id")));
        Long userId = Long.parseLong(userHelpers.getUserAttribute(request).get("user_id").toString());

        return postService.removePost(postId);
    }
}
