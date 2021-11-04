package serverapi.tables.forum.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import serverapi.api.Response;
import serverapi.query.dtos.tables.PostUserDTO;
import serverapi.query.repository.forum.CategoryRepos;
import serverapi.query.repository.forum.PostCategoryRepos;
import serverapi.query.repository.forum.PostRepos;
import serverapi.query.repository.user.UserRepos;
import serverapi.tables.forum.category.Category;
import serverapi.tables.forum.post_category.PostCategory;
import serverapi.tables.user_tables.user.User;

import java.util.*;

@Service
public class PostService {

    private final UserRepos userRepos;
    private final PostRepos postRepos;
    private final PostCategoryRepos postCategoryRepos;
    private final CategoryRepos categoryRepos;

    @Autowired
    public PostService(UserRepos userRepos, PostRepos postRepos, PostCategoryRepos postCategoryRepos, CategoryRepos categoryRepos) {
        this.userRepos = userRepos;
        this.postCategoryRepos = postCategoryRepos;
        this.postRepos = postRepos;
        this.categoryRepos = categoryRepos;
    }


    protected ResponseEntity<HashMap> createPost(Long userId, String title, String content, List<Long> listCategoryId) {
        Calendar curTime = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        Optional<User> userOptional = userRepos.findById(userId);
        if (userOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "user not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }
        User user = userOptional.get();


        // set post
        Post newPost = new Post();
        newPost.setTitle(title);
        newPost.setContent(content);
        newPost.setCount_like(0);
        newPost.setUser(user);
        newPost.setCreated_at(curTime);
        newPost.setIs_approved(true);
        newPost.setIs_deprecated(false);

        postRepos.saveAndFlush(newPost);

        // set categories
        List<Category> categories = categoryRepos.findAllById(listCategoryId);

        categories.forEach(category -> {
            PostCategory postCategory = new PostCategory();
            postCategory.setCategory(category);
            postCategory.setPost(newPost);
            postCategoryRepos.saveAndFlush(postCategory);
        });


        // response
        Map<String, Object> userMap = new HashMap<String, Object>();
        userMap.put("user_id", user.getUser_id());
        userMap.put("user_email", user.getUser_email());
        userMap.put("user_name", user.getUser_name());
        userMap.put("user_avatar", user.getUser_avatar());
        userMap.put("user_isAdmin", user.getUser_isAdmin());


        PostUserDTO postUserDTO = new PostUserDTO(
                newPost.getPost_id(),
                newPost.getTitle(),
                newPost.getContent(),
                categories,
                userMap
        );

        Map<String, Object> msg = Map.of(
                "msg", "create post OK!",
                "post", postUserDTO
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
