package serverapi.tables.forum.post;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.tables.manga_tables.manga.pojo.CommentPOJO;

@RestController
@RequestMapping("/api/forum_unauth/post")
@CacheConfig(cacheNames = {"post_unauth"})
public class PostControllerUnAuth {

    private final PostService postService;

    @Autowired
    public PostControllerUnAuth(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/getposts")
    public ResponseEntity getPosts(@RequestParam int from, @RequestParam int amount) {

        return postService.getPosts(from, amount);
    }

    @GetMapping("/getpost")
    public ResponseEntity getPost(@RequestParam String post_id) {
        Long postId = Long.parseLong(post_id);

        return postService.getPost(postId);
    }

    @Cacheable(value = "posts_category", key = "{#root.method, #category_id}")
    @GetMapping("/getposts_bycategory")
    public ResponseEntity getByCategory(@RequestParam String category_id) {
        Long categoryId = Long.parseLong(category_id);

        return postService.getByCategory(categoryId);
    }

    @PostMapping("/getcommentspost")
    public ResponseEntity getCommentsPost(@RequestBody CommentPOJO commentPOJO) {

        Long postID = Long.parseLong(commentPOJO.getPost_id());

        int from = commentPOJO.getFrom();
        System.out.println("from_" + from);

        int amount = commentPOJO.getAmount();
        System.out.println("amount_" + amount);

        return postService.getCommentsPost(postID, from, amount);
    }
}
