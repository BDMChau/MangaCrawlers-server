package serverapi.tables.forum.post;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forum_unauth/post")
public class PostControllerUnAuth {

    private final PostService postService;

    @Autowired
    public PostControllerUnAuth(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/getall")
    public ResponseEntity getAll(){
        return postService.getAll();
    }

    @GetMapping("/getpost")
    public ResponseEntity getPost(@RequestParam String post_id){
        Long postId = Long.parseLong(post_id);

        return postService.getPost(postId);
    }
}
