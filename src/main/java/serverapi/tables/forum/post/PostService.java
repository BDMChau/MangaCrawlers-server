package serverapi.tables.forum.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import serverapi.api.Response;
import serverapi.helpers.OffsetBasedPageRequest;
import serverapi.query.dtos.features.SearchCriteriaDTO;
import serverapi.query.dtos.features.MangaCommentDTOs.CommentTreesDTO;
import serverapi.query.dtos.features.MangaCommentDTOs.MangaCommentDTOs;
import serverapi.query.dtos.tables.PostUserDTO;
import serverapi.query.repository.forum.CategoryRepos;
import serverapi.query.repository.forum.PostCategoryRepos;
import serverapi.query.repository.forum.PostLikeRepos;
import serverapi.query.repository.forum.PostRepos;
import serverapi.query.repository.manga.comment.MangaCommentsRepos;
import serverapi.query.repository.user.UserRepos;
import serverapi.query.specification.Specificationn;
import serverapi.tables.forum.category.Category;
import serverapi.tables.forum.post_category.PostCategory;
import serverapi.tables.forum.post_like.PostLike;
import serverapi.tables.manga_tables.manga.MangaService;
import serverapi.tables.manga_tables.manga_comment.manga_comments.MangaComments;
import serverapi.tables.user_tables.user.User;

import java.util.*;

@Service
public class PostService {

    private final UserRepos userRepos;
    private final PostRepos postRepos;
    private final PostCategoryRepos postCategoryRepos;
    private final CategoryRepos categoryRepos;
    private final PostLikeRepos postLikeRepos;
    private final MangaCommentsRepos mangaCommentsRepos;
    private final MangaService mangaService;

    @Autowired
    public PostService(UserRepos userRepos, PostRepos postRepos, PostCategoryRepos postCategoryRepos, CategoryRepos categoryRepos, PostLikeRepos postLikeRepos, MangaCommentsRepos mangaCommentsRepos, MangaService mangaService) {
        this.userRepos = userRepos;
        this.postRepos = postRepos;
        this.postCategoryRepos = postCategoryRepos;
        this.categoryRepos = categoryRepos;
        this.postLikeRepos = postLikeRepos;
        this.mangaCommentsRepos = mangaCommentsRepos;
        this.mangaService = mangaService;
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
        PostUserDTO postUserDTO = postRepos.getByPostId(newPost.getPost_id()).get();
        List<Category> categoryList = categoryRepos.getAllByPostId(newPost.getPost_id());
        postUserDTO.setCategoryList(categoryList);

        Map<String, Object> msg = Map.of(
                "msg", "create post OK!",
                "post", postUserDTO
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    protected ResponseEntity getPosts(int from, int amount) {
        Pageable pageable = new OffsetBasedPageRequest(from, amount);
        List<PostUserDTO> posts = postRepos.getPosts(pageable);

        posts.forEach(post ->{
            List<MangaComments> cmts = mangaCommentsRepos.getCmtsByPostId(post.getPost_id());
            post.setComment_count(cmts.size());
        });

        if (posts.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "no posts!",
                    "from", 0,
                    "posts", new ArrayList<>()
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }

        posts.forEach(post -> {
            List<Category> categoryList = postCategoryRepos.getCategoriesByPostId(post.getPost_id());
            post.setCategoryList(categoryList);
        });


        Map<String, Object> msg = Map.of(
                "msg", "get posts OK!",
                "from", from + amount,
                "posts", posts
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    protected ResponseEntity getPost(Long postId) {
        Optional<PostUserDTO> postUserDTOOptional = postRepos.getByPostId(postId);
        if (postUserDTOOptional.isEmpty()) {
            Map<String, Object> err = Map.of("err", "post not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(), HttpStatus.BAD_REQUEST);
        }
        PostUserDTO postUserDTO = postUserDTOOptional.get();

        List<Category> categoryList = categoryRepos.getAllByPostId(postUserDTO.getPost_id());
        postUserDTO.setCategoryList(categoryList);

        Map<String, Object> msg = Map.of(
                "msg", "get post OK!",
                "post", postUserDTO
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }


    protected ResponseEntity getByCategory(Long categoryId) {
        Optional<Category> categoryOptional = categoryRepos.findById(categoryId);

        List<PostUserDTO> posts = postRepos.getPostsByCategory(categoryId);
        if (posts.isEmpty() || categoryOptional.isEmpty()) {
            Map<String, Object> err = Map.of(
                    "err", "no posts with this category!",
                    "category", new HashMap<>(),
                    "posts", new ArrayList<>()
            );
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, err).toJSON(), HttpStatus.ACCEPTED);
        }
        Category category = categoryOptional.get();

        posts.forEach(post -> {
            List<Category> categoryList = postCategoryRepos.getCategoriesByPostId(post.getPost_id());
            post.setCategoryList(categoryList);
        });

        Map<String, Object> msg = Map.of(
                "msg", "get posts with category OK!",
                "category", category,
                "posts", posts
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    protected ResponseEntity getCommentsPost(Long postID, int from, int amount) {

        // Initialize variable
        final String level1 = "1";
        final String level2 = "2";

        final Pageable pageable = new OffsetBasedPageRequest(from, amount);
        final Pageable childPageable = new OffsetBasedPageRequest(0, 5);

        // Check condition
        Optional<Post> postOptional = postRepos.findById(postID);
        if (postOptional.isEmpty()) {

            Map<String, Object> err = Map.of("err", "Post not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, err).toJSON(),
                    HttpStatus.BAD_REQUEST);
        }

        // get manga comments in each level
        List<MangaCommentDTOs> cmtsLv0 = mangaCommentsRepos.getPostCommentsLevel0(postID, pageable);
        if (cmtsLv0.isEmpty()) {

            Map<String, Object> msg = Map.of("msg", "No comments found!");
            return new ResponseEntity<>(new Response(202, HttpStatus.ACCEPTED, msg).toJSON(), HttpStatus.ACCEPTED);
        }

        // Get comment
        //set tags for each comment
        List<MangaCommentDTOs> comments;
        cmtsLv0.forEach(lv0 -> {

            lv0 = mangaService.setListTags(lv0);

            //get child comments
            List<CommentTreesDTO> cmtsLv1 = mangaCommentsRepos.getCommentsChild(lv0.getManga_comment_id(), level1, childPageable);
            List<CommentTreesDTO> cmtsLv2 = mangaCommentsRepos.getCommentsChild(lv0.getManga_comment_id(), level2, childPageable);

            MangaCommentDTOs finalLv0 = lv0;
            cmtsLv1.forEach(lv01 -> {

                CommentTreesDTO finalLv01 = lv01;
                cmtsLv2.forEach(lv02 -> {

                    lv02 = mangaService.setListTags(lv02);

                    if (finalLv0.getManga_comment_id() == lv02.getParent_id()) {

                        finalLv01.getComments_level_02().add(lv02);
                    }
                });

                lv01 = mangaService.setListTags(lv01);

                if (finalLv0.getManga_comment_id() == lv01.getParent_id()) {

                    finalLv0.getComments_level_01().add(lv01);
                }
            });
        });
        comments = cmtsLv0;

        Map<String, Object> msg = Map.of(
                "msg", "Get post's comments successfully!",
                "post_info", postOptional,
                "don't use these param", "manga_comment_relation_id, parent_id, child_id, level, manga_comment_tag_id",
                "comments", comments
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);

    }


    protected ResponseEntity searchByTitle(String valToSearch) {
        Specificationn specificationn = new Specificationn(new SearchCriteriaDTO("title", ":", valToSearch));
        Specificationn.SearchingPosts searchingPosts = specificationn.new SearchingPosts();

        List<Post> results = postRepos.findAll(searchingPosts);


        List<PostUserDTO> listToRes = new ArrayList();
        results.forEach(post -> {
            PostUserDTO postUserDTO = new PostUserDTO(post.getPost_id(), post.getTitle(), null, post.getCount_like(), post.getCreated_at(), null, null, null, null, null);

            List<Category> categoryList = categoryRepos.getAllByPostId(post.getPost_id());
            postUserDTO.setCategoryList(categoryList);

            listToRes.add(postUserDTO);
        });


        Map<String, Object> msg = Map.of(
                "msg", "search posts OK!",
                "posts", listToRes
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity checkUserLike(Long userID, Long postID) {
        int likeStatus = 1;
        String sLikeStatus = "";
        Optional<PostLike> postLikeOptional = postLikeRepos.getPostLike(postID, userID);

        if (postLikeOptional.isEmpty()) {
            likeStatus = 0;
        }
        switch (likeStatus) {
            case 0 -> sLikeStatus = "hasn't liked";
            case 1 -> sLikeStatus = "liked";
        }

        Map<String, Object> msg = Map.of(
                "msg", "Check like successfully!",
                "status_number", likeStatus,
                "status", sLikeStatus
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity getTotalLike(Long postID) {
        Optional<Post> postOptional = postRepos.findById(postID);
        if (postOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Post not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        Map<String, Object> msg = Map.of(
                "msg", "Get total like successfully!",
                "total_like", postOptional.get().getCount_like()
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }

    public ResponseEntity addLike(Long userID, Long postID) {
        Optional<Post> postOptional = postRepos.findById(postID);
        Optional<User> userOptional = userRepos.findById(userID);
        if (postOptional.isEmpty() || userOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Post or user is not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        Post post = postOptional.get();
        post.setCount_like(post.getCount_like() + 1);

        PostLike postLike = new PostLike();
        postLike.setPost(post);
        postLike.setUser(userOptional.get());

        postRepos.saveAndFlush(post);
        postLikeRepos.saveAndFlush(postLike);
        int countLikes = post.getCount_like();

        Map<String, Object> msg = Map.of(
                "msg", "Like successfully!",
                "likes", countLikes
        );
        return new ResponseEntity<>(new Response(201, HttpStatus.CREATED, msg).toJSON(), HttpStatus.CREATED);
    }

    public ResponseEntity unlike(Long userID, Long postID) {
        Optional<PostLike> postLikeOptional = postLikeRepos.getPostLike(postID, userID);
        Optional<Post> postOptional = postRepos.findById(postID);
        if (postLikeOptional.isEmpty() || postOptional.isEmpty()) {
            Map<String, Object> msg = Map.of("err", "Like is not found!");
            return new ResponseEntity<>(new Response(400, HttpStatus.BAD_REQUEST, msg).toJSON(), HttpStatus.BAD_REQUEST);
        }

        PostLike postLike = postLikeOptional.get();
        Post post = postOptional.get();

        if (post.getCount_like() > 0) {
            post.setCount_like(post.getCount_like() - 1);
        }
        postRepos.saveAndFlush(post);
        postLikeRepos.delete(postLike);
        int countLikes = post.getCount_like();

        Map<String, Object> msg = Map.of(
                "msg", "Unlike successfully!",
                "likes", countLikes
        );
        return new ResponseEntity<>(new Response(200, HttpStatus.OK, msg).toJSON(), HttpStatus.OK);
    }
}
