package serverapi.tables.forum.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.query.repository.forum.CategoryRepos;
import serverapi.query.repository.forum.PostCategoryRepos;
import serverapi.tables.forum.post_category.PostCategory;

import java.util.List;
import java.util.Map;

@Service
public class CategoryService {
    private final CategoryRepos categoryRepos;

    @Autowired
    public CategoryService(CategoryRepos categoryRepos) {
        this.categoryRepos = categoryRepos;
    }



    protected ResponseEntity getAll() {
        List<Category> categoryList = categoryRepos.findAll();
        if (categoryList.isEmpty()) {
            Map<String, Object> err = Map.of("err", "No categories!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get all categories OK!",
                "categories", categoryList
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
