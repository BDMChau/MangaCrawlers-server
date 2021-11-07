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


}
