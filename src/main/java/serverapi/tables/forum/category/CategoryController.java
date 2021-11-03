package serverapi.tables.forum.category;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import serverapi.tables.forum.post_category.PostCategoryService;
import serverapi.tables.manga_tables.genre.GenreService;

@RestController
@RequestMapping("/api/forum_unauth/category")
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/getall")
    public ResponseEntity getAll(){

        return categoryService.getAll();
    }
}
