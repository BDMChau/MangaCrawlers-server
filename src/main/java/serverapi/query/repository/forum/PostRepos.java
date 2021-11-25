package serverapi.query.repository.forum;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import serverapi.query.dtos.tables.PostUserDTO;
import serverapi.tables.forum.post.Post;

import java.util.List;
import java.util.Optional;


@Repository
public interface PostRepos extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {


    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(pr.parent_id.post_id,
            post.post_id, post.title, post.content,post.count_like, post.count_dislike, post.created_at,
            user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
            )
            FROM Post post
            LEFT JOIN PostRelation pr on post.post_id = pr.child_id.post_id
            JOIN User user ON user.user_id = post.user.user_id
            WHERE post.post_id = ?1 AND post.is_deprecated = false AND post.is_approved = true
            """)
    Optional<PostUserDTO> getByPostId(Long postId);

    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(
            post.post_id, post.title, post.content, post.count_like, post.count_dislike, post.created_at,
            user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
            )
            FROM Post post
            JOIN User user ON user.user_id = post.user.user_id 
            WHERE post.is_deprecated = false AND post.is_approved = true
            ORDER BY post.created_at DESC
            """)
    List<PostUserDTO> getPosts(Pageable pageable);

    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(pr.parent_id.post_id,
            post.post_id, post.title, post.content, post.count_like, post.count_dislike, post.is_deprecated, post.is_approved, post.created_at,
            user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
            )
            FROM Post post
            LEFT JOIN PostRelation pr on post.post_id = pr.child_id.post_id
            JOIN User user ON user.user_id = post.user.user_id 
            ORDER BY post.created_at
            """)
    List<PostUserDTO> getAllPostsAndUserInfo(); // for admin


    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(post.post_id,
            post.title, post.content, COUNT(cmt.comment_id), post.count_like, post.count_dislike, post.created_at,
            user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
            )
            FROM Post post
            JOIN User user ON user.user_id = post.user.user_id
            LEFT JOIN Comment cmt ON cmt.post.post_id = post.post_id
            WHERE user.user_id = ?1 AND post.is_deprecated = false AND post.is_approved = true
            GROUP BY post.post_id, post.title, post.content,  post.count_like, post.count_dislike, post.created_at,
            user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
            ORDER BY post.created_at
            """)
    Page<PostUserDTO> getPostsByUserId(Long userId, Pageable pageable);


    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(pr.parent_id.post_id,
                      post.post_id, post.title, post.content, post.count_like, post.count_dislike, post.created_at,
                      user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
                      )
                      FROM Post post
                      LEFT JOIN PostRelation pr on post.post_id = pr.child_id.post_id
                      JOIN User user ON user.user_id = post.user.user_id
                      JOIN PostCategory post_cate ON post_cate.post.post_id = post.post_id
                      WHERE post_cate.category.category_id = ?1 AND post.is_deprecated = false AND post.is_approved = true
                      ORDER BY post.created_at
                      """)
    Page<PostUserDTO> getPostsByCategory(Long categoryId, Pageable pageable);


    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(
                      post.post_id, post.title, post.content, COUNT(cmt.comment_id), post.count_like, post.count_dislike, post.created_at,
                      user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
                      )
                      FROM Post post
                      JOIN User user ON user.user_id = post.user.user_id
                      JOIN Comment cmt ON cmt.post.post_id = post.post_id
                      WHERE post.created_at >= (current_date - (:from_time)) AND post.created_at < (current_date - (:to_time)) 
                      AND post.is_deprecated = false AND post.is_approved = true
                      GROUP BY post.post_id, post.title, post.content, post.count_like, post.count_dislike, post.created_at,
                                user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
                      HAVING COUNT(cmt.comment_id) > 0
                      ORDER BY COUNT(cmt.comment_id) DESC    
            """)
    List<PostUserDTO> getTopPostsNumberOfCmts(Pageable pageable, @Param("from_time") int from_time, @Param("to_time") int to_time);


    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(
                      post.post_id, post.title, post.content, post.count_like, post.count_dislike, post.created_at,
                      user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
                      )
                      FROM Post post
                      JOIN User user ON user.user_id = post.user.user_id
                      WHERE post.created_at >= (current_date - (:from_time)) AND post.created_at < (current_date - (:to_time)) AND post.count_like > 0
                      AND post.is_deprecated = false AND post.is_approved = true
                      GROUP BY post.post_id, post.title, post.content, post.count_like, post.count_dislike, post.created_at,
                                user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
                      ORDER BY post.count_like DESC
            """)
    List<PostUserDTO> getTopPostsLike(Pageable pageable, @Param("from_time") int from_time, @Param("to_time") int to_time);


    @Query("""
            SELECT new serverapi.query.dtos.tables.PostUserDTO(
                      post.post_id, post.title, post.content, post.count_like, post.count_dislike, post.created_at,
                      user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
                      )
                      FROM Post post
                      JOIN User user ON user.user_id = post.user.user_id
                      WHERE post.created_at >= (current_date - (:from_time)) AND post.created_at < (current_date - (:to_time)) AND post.count_dislike > 0
                      AND post.is_deprecated = false AND post.is_approved = true
                      GROUP BY post.post_id, post.title, post.content, post.count_like, post.count_dislike, post.created_at,
                                user.user_id, user.user_name, user.user_email, user.user_avatar, user.user_isAdmin
                      ORDER BY post.count_dislike DESC
            """)
    List<PostUserDTO> getTopPostsDislike(Pageable pageable, @Param("from_time") int from_time, @Param("to_time") int to_time);

}
