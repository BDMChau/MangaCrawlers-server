package serverapi.tables.forum.post_category;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serverapi.tables.manga_tables.genre.GenreService;

@RestController
@RequestMapping("/api/forum/post-category")
public class PostCategoryController {

    private final PostCategoryService postCategoryService;

    @Autowired
    public PostCategoryController(PostCategoryService postCategoryService) {
        this.postCategoryService = postCategoryService;
    }


}
