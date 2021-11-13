package serverapi.tables.forum.post;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serverapi.tables.manga_tables.manga.pojo.CommentPOJO;

import java.util.Map;

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

    @GetMapping("/getsuggestion")
    public ResponseEntity getSuggestion(@RequestParam int quantity) {

        return postService.getSuggestion(quantity);
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

    @GetMapping("/search")
    public ResponseEntity searchByTitle(@RequestParam String value) {

        return postService.searchByTitle(value);
    }

    @GetMapping("/get_top_post_cmts")
    public ResponseEntity getTopPostsCmts(@RequestParam int quantity) {

        return postService.getTopPostsCmts(quantity);
    }

    @GetMapping("/get_top_post_like")
    public ResponseEntity getTopPostsLike(@RequestParam int quantity) {

        return postService.getTopPostsLike(quantity);
    }

    @GetMapping("/get_top_post_dislike")
    public ResponseEntity getTopPostsDislike(@RequestParam int quantity) {

        return postService.getTopPostsDislike(quantity);
    }

    @PostMapping("/get_total_like")
    public ResponseEntity getTotalLike(@RequestBody Map data) {
        String post_id = String.valueOf(data.get("post_id"));

        Long posttID = 0L;
        if (!post_id.isEmpty()) {
            posttID = Long.parseLong(post_id);
        }

        return postService.getTotalLike(posttID);
    }

    @PostMapping("/get_total_dislike")
    public ResponseEntity getTotalDislike(@RequestBody Map data) {
        String post_id = String.valueOf(data.get("post_id"));

        Long posttID = 0L;
        if (!post_id.isEmpty()) {
            posttID = Long.parseLong(post_id);
        }

        return postService.getTotalDislike(posttID);
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
