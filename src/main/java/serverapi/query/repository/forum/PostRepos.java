package serverapi.query.repository.forum;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.AuthorMangaDTO;
import serverapi.query.dtos.tables.MangaGenreDTO;
import serverapi.query.dtos.tables.PostUserDTO;
import serverapi.tables.forum.post.Post;
import serverapi.tables.forum.post_category.PostCategory;
import serverapi.tables.user_tables.user.User;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepos extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {


    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(
            post.post_id, post.title, post.content,post.count_like, post.created_at,
            user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
            )
            FROM Post post
            JOIN User user ON user.user_id = post.user.user_id
            WHERE post.post_id = ?1 AND post.is_deprecated = false AND post.is_approved = true
            """)
    Optional<PostUserDTO> getByPostId(Long postId);

    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(
            post.post_id, post.title, post.content, post.count_like, post.created_at,
            user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
            )
            FROM Post post
            JOIN User user ON user.user_id = post.user.user_id 
            WHERE post.is_deprecated = false AND post.is_approved = true
            ORDER BY post.created_at
            """)
    List<PostUserDTO> getPosts(Pageable pageable);

    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(
            post.post_id, post.title, post.content, post.count_like ,post.is_deprecated, post.is_approved, post.created_at,
            user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
            )
            FROM Post post
            JOIN User user ON user.user_id = post.user.user_id 
            ORDER BY post.created_at
            """)
    List<PostUserDTO> getAllPostsAndUserInfo();


    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(
            post.post_id, post.title, post.content, post.count_like, post.created_at,
            user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
            )
            FROM Post post
            JOIN User user ON user.user_id = post.user.user_id
            WHERE user.user_id = ?1 AND post.is_deprecated = false AND post.is_approved = true
            ORDER BY post.created_at
            """)
    List<PostUserDTO> getPostsByUserId(Long userId, Pageable pageable);


    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(
                      post.post_id, post.title, post.content, post.count_like, post.created_at,
                      user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
                      )
                      FROM Post post
                      JOIN User user ON user.user_id = post.user.user_id
                      JOIN PostCategory post_cate ON post_cate.post.post_id = post.post_id
                      WHERE post_cate.category.category_id = ?1 AND post.is_deprecated = false AND post.is_approved = true
                      ORDER BY post.created_at
                      """)
    List<PostUserDTO> getPostsByCategory(Long categoryId);

    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(
                      post.post_id, post.title, post.content, COUNT(cmt.manga_comment_id),post.count_like, post.created_at,
                      user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
                      )
                      FROM Post post
                      JOIN User user ON user.user_id = post.user.user_id
                      JOIN MangaComments cmt ON cmt.post.post_id = post.post_id
                      WHERE DATE(post.created_at) >= (CURRENT_DATE - :from_time) and DATE(post.created_at) < (CURRENT_DATE - :to_time)
                      GROUP BY post.post_id, post.title, post.content, post.count_like, post.created_at,
                                user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
                    
                      ORDER BY COUNT(cmt.manga_comment_id) DESC    
            """)
    List<PostUserDTO> getTopPostsNumberOfCmts(Pageable pageable);


    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(
                      post.post_id, post.title, post.content, post.count_like, post.created_at,
                      user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
                      )
                      FROM Post post
                      JOIN User user ON user.user_id = post.user.user_id
                      GROUP BY post.post_id, post.title, post.content, post.count_like, post.created_at,
                                user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
                      ORDER BY post.count_like DESC
            """)
    List<PostUserDTO> getTopPostsLike(Pageable pageable);

}
