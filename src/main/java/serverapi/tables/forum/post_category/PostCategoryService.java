package serverapi.tables.forum.post_category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.query.repository.forum.PostCategoryRepos;
import serverapi.query.repository.manga.GenreRepos;

import java.util.List;
import java.util.Map;

@Service
public class PostCategoryService {
    private final PostCategoryRepos postCategoryRepos;

    @Autowired
    public PostCategoryService(PostCategoryRepos postCategoryRepos) {
        this.postCategoryRepos = postCategoryRepos;
    }


    protected ResponseEntity getAll() {
        List<PostCategory> postCategoryList = postCategoryRepos.findAll();
        if (postCategoryList.isEmpty()) {
            Map<String, Object> err = Map.of("err", "No categories!");
            return new ResponseEntity<>(new Response(204, HttpStatus.NO_CONTENT, err).toJSON(), HttpStatus.NO_CONTENT);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get all categories OK!",
                "categories", postCategoryList
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

}
